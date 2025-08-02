package com.sunbeam.services.impl;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.sunbeam.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

@Override
public void sendVerificationOtpEmail(String userEmail, String subject, String text) throws MessagingException {
    try {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true); // Set to true if your text contains HTML
        mimeMessageHelper.setTo(userEmail);
        
        javaMailSender.send(mimeMessage);
    } catch (MailException e) {
        // Log the exception for debugging purposes
        System.err.println("Failed to send email to " + userEmail + ": " + e.getMessage());
        throw new MessagingException("Failed to send email.", e);
    }
}
}