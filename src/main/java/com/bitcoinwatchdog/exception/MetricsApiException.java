package com.bitcoinwatchdog.exception;

public class MetricsApiException extends RuntimeException{

	public MetricsApiException(String message) {
		super(message);
	}
	
	public MetricsApiException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
