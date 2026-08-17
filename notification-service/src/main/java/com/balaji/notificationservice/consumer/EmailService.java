package com.balaji.notificationservice.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    
    public EmailService (JavaMailSender mailSender) {
    	this.mailSender = mailSender;
    }

    public void sendNotificationEmail(String to, String subject, String body) {
        if (mailSender == null) {
            log.warn("⚠️ Mail configuration is missing. Simulating sending email to [{}]: {}", to, body);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@yourdomain.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("✅ Email has been successfully delivered to: {}", to);
            
        } catch (Exception e) {
            log.error("❌ Critical SMTP routing failure while messaging user: " + to, e);
        }
    }
}
