package com.sunbeam.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationOtpEmail(String userEmail, String subject, String text) throws MessagingException;
}