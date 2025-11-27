package com.test.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoExtratoDTO {

    private Integer valor;
    private String tipo;
    private String descricao;
    private OffsetDateTime realizada_em;
}
