package com.test.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false, length = 1)
    private String tipo; // "c" ou "d"

    @Column(nullable = false, length = 10)
    private String descricao;

    @Column(nullable = false)
    private OffsetDateTime realizadaEm;

    public Transacao(Cliente cliente,
                     Integer valor,
                     String tipo,
                     String descricao,
                     OffsetDateTime realizadaEm) {

        this.cliente = cliente;
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = realizadaEm;
    }
}
