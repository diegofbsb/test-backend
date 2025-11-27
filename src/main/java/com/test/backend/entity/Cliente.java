package com.test.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    private Integer id;

    @Column(nullable = false)
    private Integer limite;

    @Column(nullable = false)
    private Integer saldo;

    public Cliente(int id, Integer limite, Integer saldo) {
        this.id = id;
        this.limite = limite;
        this.saldo = saldo;
    }
}
