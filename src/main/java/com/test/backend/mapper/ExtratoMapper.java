package com.test.backend.mapper;

import com.test.backend.dto.SaldoDTO;
import com.test.backend.dto.TransacaoExtratoDTO;
import com.test.backend.entity.Cliente;
import com.test.backend.entity.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExtratoMapper {

    @Mapping(target = "data_extrato", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "total", source = "saldo")
    SaldoDTO toSaldoDTO(Cliente cliente);

    @Mapping(target = "realizada_em", source = "realizadaEm")
    TransacaoExtratoDTO toTransacaoDTO(Transacao transacao);

    List<TransacaoExtratoDTO> toTransacoesDTO(List<Transacao> transacoes);
}
