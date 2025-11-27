package com.test.backend.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NovaTransacaoRequest {

    @NotNull
    @Positive
    private Integer valor;

    @NotBlank
    @Pattern(regexp = "^[cCdD]$")
    private String tipo;

    @NotBlank
    @Size(min = 1, max = 10)
    private String descricao;
}
