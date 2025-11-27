package com.test.backend.controller;

import com.test.backend.request.NovaTransacaoRequest;
import com.test.backend.response.ExtratoResponse;
import com.test.backend.response.TransacaoResponse;
import com.test.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações de transações e extrato")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(
            summary = "Registrar nova transação",
            description = "Crédito (c) ou débito (d) para um cliente. Retorna saldo atualizado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transação registrada com sucesso",
                            content = @Content(schema = @Schema(implementation = TransacaoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Transação inválida ou excede limite"
                    )
            }
    )
    @PostMapping("/{id}/transacoes")
    @ResponseStatus(HttpStatus.OK)
    public TransacaoResponse novaTransacao(
            @Parameter(description = "ID do cliente") @PathVariable("id") Integer id,
            @Valid @RequestBody NovaTransacaoRequest request) {

        return service.novaTransacao(id, request);
    }

    @Operation(
            summary = "Consultar extrato",
            description = "Retorna saldo atual e até 10 últimas transações.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Extrato retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = ExtratoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado"
                    )
            }
    )
    @GetMapping("/{id}/extrato")
    public ExtratoResponse extrato(
            @Parameter(description = "ID do cliente") @PathVariable("id") Integer id) {

        return service.extrato(id);
    }
}
