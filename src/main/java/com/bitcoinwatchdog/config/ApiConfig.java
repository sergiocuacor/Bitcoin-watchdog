package com.bitcoinwatchdog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfig {

    @Bean
     WebClient webClient() {
        return WebClient.builder()
            .defaultHeader("Accept", "application/json")
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024))
            .build();
    }
}