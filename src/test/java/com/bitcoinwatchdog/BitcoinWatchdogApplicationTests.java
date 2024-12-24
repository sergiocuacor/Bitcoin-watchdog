package com.bitcoinwatchdog;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

import com.bitcoinwatchdog.metrics.client.MetricsApiClient;

@SpringBootTest
class BitcoinWatchdogApplicationTests {

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public MetricsApiClient metricsApiClient() {
            return mock(MetricsApiClient.class);
        }
    }

    @Test
    void contextLoads() {
        // Este test solo verifica que el contexto de Spring se carga correctamente
    }
}