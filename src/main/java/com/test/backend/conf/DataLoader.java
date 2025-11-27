package com.test.backend.conf;

import com.test.backend.entity.Cliente;
import com.test.backend.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initClientes(ClienteRepository clienteRepository) {
        return args -> {
            if (clienteRepository.count() == 0) {
                clienteRepository.save(new Cliente(1, 100000, 0));
                clienteRepository.save(new Cliente(2, 80000, 0));
                clienteRepository.save(new Cliente(3, 1000000, 0));
                clienteRepository.save(new Cliente(4, 10000000, 0));
                clienteRepository.save(new Cliente(5, 500000, 0));
            }
        };
    }
}
