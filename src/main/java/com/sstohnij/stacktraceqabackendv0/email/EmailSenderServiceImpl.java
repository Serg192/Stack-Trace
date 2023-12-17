package com.sstohnij.stacktraceqabackendv0.email;

import com.sstohnij.stacktraceqabackendv0.email.template.EmailTemplateType;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Transient;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSender {

    @Value("${stack-trace.mail.username}")
    private String email;
    @Value("${stack-trace.mail.password}")
    private String password;

    private final TemplateEngine templateEngine;
    @Override
    public void sendEmail(String to, String subject, String message) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            Transport.send(msg);
        } catch (Exception e) {
            log.error("Email sending error: ", e);
        }

    }

    @Override
    public void sendEmailUsingTemplate(String to, String subject, EmailTemplateType emailTemplateType, Context context) {
        String page = templateEngine.process(emailTemplateType.getTemplate(), context);
        sendEmail(to, subject, page);
    }
}