package com.radha.smartcommerce.mail;

public interface MailService {
    void sendMail (String to, String subject, String body);
}
