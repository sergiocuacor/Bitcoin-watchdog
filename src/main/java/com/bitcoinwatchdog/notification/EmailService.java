package com.bitcoinwatchdog.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	// Spring inyectará el JavaMailSender configurado
	private final JavaMailSender emailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Value("${spring.mail.username}")
	private String toEmail;
	
	public EmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}
	
	public void sendAlert(String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            emailSender.send(mailMessage);
            logger.info("Alert email sent successfully to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send email alert", e);
            throw new EmailSendException("Failed to send email alert", e);
        }
    }
	
}
