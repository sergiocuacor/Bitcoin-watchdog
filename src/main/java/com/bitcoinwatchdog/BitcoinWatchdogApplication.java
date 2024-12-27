package com.bitcoinwatchdog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BitcoinWatchdogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinWatchdogApplication.class, args)
		.close();
	}

}
