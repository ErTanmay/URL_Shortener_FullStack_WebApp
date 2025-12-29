package com.tsl.urlshortner.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class EmailService {
	
	private JavaMailSender emailSender;
	
	public EmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Async
	public void sendVerificationEmail(String to, String subject, String text) 
			throws MessagingException{
		
		log.info("To : {}, Subject : {}, Text : {}", to, subject, text);
		
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		emailSender.send(message);
		
	}

}
