package com.test.backend.conf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Rinha de Backend - Transações")
                        .description("API de transações e extrato do cliente")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("email@exemplo.com")));
    }
}
