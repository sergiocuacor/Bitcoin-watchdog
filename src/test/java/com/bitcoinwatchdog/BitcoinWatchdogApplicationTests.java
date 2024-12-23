package com.bitcoinwatchdog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.mail.host=smtp.gmail.com",
    "spring.mail.port=587",
    "spring.mail.username=test@example.com",
    "spring.mail.password=test-password",
    "email.from=test@example.com",
    "email.to=test@example.com",
    "api.base.url=http://localhost:8080"
})
class BitcoinWatchdogApplicationTests {

	@Test
    @Disabled("Disabled until we implement proper test configuration")
    void contextLoads() {
    }
}