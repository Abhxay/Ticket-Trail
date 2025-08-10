package com.example.TicketTrail.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TicketTrail API")
                        .version("1.0")
                        .description("API for request management - raise requests, mark done, fetch requests")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server")
                ));
    }
}
