package id.ac.pnj.kanban.mail;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendMimeMail(String to, String subject, String text) throws MessagingException;
}
