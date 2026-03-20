package com.test.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void enviar() {
        kafkaTemplate.send("pagamentos", "mensagem teste");
    }

    @KafkaListener(topics = "pagamentos", groupId = "rinha-group")
    public void consumir(String mensagem) {
        System.out.println("Recebido: " + mensagem);
    }
}
