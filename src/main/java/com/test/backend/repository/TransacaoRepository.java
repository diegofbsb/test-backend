package com.test.backend.repository;

import com.test.backend.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findTop10ByClienteIdOrderByRealizadaEmDesc(Integer clienteId);

}
