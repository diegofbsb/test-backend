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
import com.test.backend.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;
    private final ExtratoMapper extratoMapper;

    public ClienteService(ClienteRepository clienteRepository,
                          TransacaoRepository transacaoRepository, ExtratoMapper extratoMapper) {
        this.clienteRepository = clienteRepository;
        this.transacaoRepository = transacaoRepository;
        this.extratoMapper = extratoMapper;
    }

    private static final String DEBITO = "d";
    private static final String CREDITO = "c";

    @Transactional
    public TransacaoResponse novaTransacao(Integer clienteId, NovaTransacaoRequest request) {

        Cliente cliente = clienteRepository.findWithLockingById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontradoException(clienteId));
        Integer valor = request.getValor();
        String tipo = request.getTipo();
        Integer saldoAtual = cliente.getSaldo();
        int novoSaldo;
        if (CREDITO.equals(Util.toLower(tipo))) {
            novoSaldo = saldoAtual + valor;
        } else if (DEBITO.equals(Util.toLower(tipo))) {
            novoSaldo = saldoAtual - valor;
            if (novoSaldo < -cliente.getLimite()) {
                throw new SaldoInsuficienteException();
            }
        } else {
            throw new IllegalArgumentException("Tipo inválido");
        }
        cliente.setSaldo(novoSaldo);
        Transacao transacao = new Transacao(
                cliente,
                valor,
                tipo,
                request.getDescricao(),
                OffsetDateTime.now()
        );
        clienteRepository.save(cliente);
        transacaoRepository.save(transacao);
        return new TransacaoResponse(cliente.getLimite(), novoSaldo);
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
