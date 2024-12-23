package com.bitcoinwatchdog.metrics.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.web.reactive.function.client.WebClient;

import com.bitcoinwatchdog.metrics.model.PuellMultiple;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class MetricsApiClientTest {

	private static MockWebServer mockWebServer;
	private MetricsApiClient metricsApiClient;
	
	@BeforeAll
	static void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}
	
	@AfterAll
	static void tearDown() throws IOException {
		mockWebServer.shutdown();
	}
	
	@BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder().build();
        metricsApiClient = new MetricsApiClient(webClient, baseUrl);
    }
	
	@Test
    void getPuellMultiple_ShouldReturnData_WhenApiResponseIsValid() {
        // Given
        String validResponse = """
            [{
                "d": "2024-01-01",
                "unixTs": "1704067200",
                "puellMultiple": "0.5"
            }]
            """;
        
        mockWebServer.enqueue(new MockResponse()
            .setBody(validResponse)
            .addHeader("Content-Type", "application/json"));

        // When
        List<PuellMultiple> result = metricsApiClient.getPuellMultiple(LocalDate.of(2024, 1, 1));

        // Then
        
        assertNotNull(result);
        assertEquals(1, result.size());
        PuellMultiple metric = result.get(0);
        assertEquals("2024-01-01", metric.getDate());
        assertEquals("1704067200", metric.getUnixTs());
        assertEquals("0.5", metric.getPuellMultiple());
    }
	
}
