package com.test.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaldoDTO {

    private Integer total;
    private OffsetDateTime data_extrato;
    private Integer limite;
}
