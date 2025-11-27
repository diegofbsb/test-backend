package com.test.backend.repository;

import com.test.backend.entity.Cliente;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cliente> findWithLockingById(Integer id);
}
