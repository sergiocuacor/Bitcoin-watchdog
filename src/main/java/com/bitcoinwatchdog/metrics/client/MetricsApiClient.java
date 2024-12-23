package com.bitcoinwatchdog.metrics.client;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.bitcoinwatchdog.metrics.model.MVRVZScore;
import com.bitcoinwatchdog.metrics.model.NUPL;
import com.bitcoinwatchdog.metrics.model.PuellMultiple;

@Component
public class MetricsApiClient {

	private static final Logger logger = LoggerFactory.getLogger(MetricsApiClient.class);
	
	private final WebClient webClient;	
	private final String baseUrl;
	
	public MetricsApiClient(WebClient webClient, @Value("${api.base.url}") String baseUrl) {
		this.webClient = webClient;
		this.baseUrl = baseUrl;
	}
	
	public List<PuellMultiple> getPuellMultiple(LocalDate date){
		String url = buildUrl("/puell-multiple", date);
	    logger.info("Calling API: {}", url);
	    
	    List<PuellMultiple> response = webClient.get()
	        .uri(url)
	        .retrieve()
	        .bodyToMono(new ParameterizedTypeReference<List<PuellMultiple>>() {})
	        .block(Duration.ofSeconds(10));
	        
	    logger.info("Puell API response: {}", response);
	    return response;
	}
	
	public List<NUPL> getNUPL(LocalDate date) {
        String url = buildUrl("/nupl", date);
        List<NUPL> response = webClient.get()
    	        .uri(url)
    	        .retrieve()
    	        .bodyToMono(new ParameterizedTypeReference<List<NUPL>>() {})
    	        .block(Duration.ofSeconds(10));
    	        
    	    logger.info("NUPL API response: {}", response);
    	    return response;
    }
	
    public List<MVRVZScore> getMVRVZ(LocalDate date) {
        String url = buildUrl("/mvrv-zscore", date);
        List<MVRVZScore> response = webClient.get()
    	        .uri(url)
    	        .retrieve()
    	        .bodyToMono(new ParameterizedTypeReference<List<MVRVZScore>>() {})
    	        .block(Duration.ofSeconds(10));
    	        
    	    logger.info("MVRV-Z Score API response: {}", response);
    	    return response;
    }

	
	
	private String buildUrl(String endpoint, LocalDate date) {
		return UriComponentsBuilder.fromUriString(baseUrl+endpoint)
				.queryParam("startday", date.toString())
				.queryParam("endday", date.toString())
				.toUriString();
				}
}
