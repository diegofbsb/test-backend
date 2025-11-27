package com.test.backend.service;

import com.test.backend.entity.Cliente;
import com.test.backend.entity.Transacao;
import com.test.backend.exception.ClienteNaoEncontradoException;
import com.test.backend.exception.SaldoInsuficienteException;
import com.test.backend.mapper.ExtratoMapper;
import com.test.backend.repository.ClienteRepository;
import com.test.backend.repository.TransacaoRepository;
import com.test.backend.request.NovaTransacaoRequest;
import com.test.backend.response.ExtratoResponse;
import com.test.backend.response.TransacaoResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class ClienteService {

    private static final String DEBITO = "d";
    private static final String CREDITO = "c";

    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;
    private final ExtratoMapper extratoMapper;
    private final JdbcTemplate jdbcTemplate;

    public ClienteService(ClienteRepository clienteRepository,
                          TransacaoRepository transacaoRepository,
                          ExtratoMapper extratoMapper,
                          JdbcTemplate jdbcTemplate) {
        this.clienteRepository = clienteRepository;
        this.transacaoRepository = transacaoRepository;
        this.extratoMapper = extratoMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    private record LimiteSaldo(int limite, int saldo) {}

    @Transactional
    public TransacaoResponse novaTransacao(Integer clienteId, NovaTransacaoRequest request) {

        String tipo = request.getTipo();
        if (tipo == null) throw new IllegalArgumentException("Tipo não pode ser nulo");
        tipo = tipo.toLowerCase(Locale.ROOT);
        if (!CREDITO.equals(tipo) && !DEBITO.equals(tipo))
            throw new IllegalArgumentException("Tipo inválido: " + request.getTipo());
        int valor = request.getValor();
        boolean clienteExiste = Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM clientes WHERE id = ?)",
                        Boolean.class,
                        clienteId
                )
        );
        if (!clienteExiste) throw new ClienteNaoEncontradoException(clienteId);
        int delta = CREDITO.equals(tipo) ? valor : -valor;
        String sqlUpdate = """
                UPDATE clientes
                SET saldo = saldo + ?
                WHERE id = ?
                  AND saldo + ? >= -limite
                RETURNING limite, saldo
                """;
        LimiteSaldo limiteSaldo = jdbcTemplate.query(
                sqlUpdate,
                ps -> {
                    ps.setInt(1, delta);
                    ps.setInt(2, clienteId);
                    ps.setInt(3, delta);
                },
                rs -> rs.next() ? new LimiteSaldo(rs.getInt("limite"), rs.getInt("saldo")) : null
        );
        if (limiteSaldo == null) throw new SaldoInsuficienteException();
        OffsetDateTime agora = OffsetDateTime.now();
        jdbcTemplate.update("""
                INSERT INTO transacoes (cliente_id, valor, tipo, descricao, realizada_em)
                VALUES (?, ?, ?, ?, ?)
                """,
                clienteId,
                valor,
                tipo,
                request.getDescricao(),
                agora
        );
        return new TransacaoResponse(
                limiteSaldo.limite(),
                limiteSaldo.saldo()
        );
    }

    @Transactional(readOnly = true)
    public ExtratoResponse extrato(Integer clienteId) {

        Cliente cliente = clienteRepository
                .findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
        List<Transacao> ultimas =
                transacaoRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(clienteId);
        var saldoDTO = extratoMapper.toSaldoDTO(cliente);
        saldoDTO.setData_extrato(OffsetDateTime.now());
        var transacoesDTO = extratoMapper.toTransacoesDTO(ultimas);
        return new ExtratoResponse(saldoDTO, transacoesDTO);
    }
}
