package com.test.backend.response;

import com.test.backend.dto.SaldoDTO;
import com.test.backend.dto.TransacaoExtratoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtratoResponse {

    private SaldoDTO saldo;
    private List<TransacaoExtratoDTO> ultimas_transacoes;

}
