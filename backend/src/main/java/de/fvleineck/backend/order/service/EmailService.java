package de.fvleineck.backend.order.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private static final Logger logger = Logger.getLogger(EmailService.class.getName());
	private static final String FROM_ADDRESS ="bestellung@fv-leineck.de" ;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// Send an email to the customer with the order confirmation
	public void sendOrderConfirmation(String to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(FROM_ADDRESS);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		try {
			mailSender.send(message);
			logger.info("Email successfully sent to: " + to);
		} catch (Exception e) {
			logger.severe("Failed to send email to: " + to + " Error: " + e.getMessage());
			throw e;
		}
	}
}