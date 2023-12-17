package com.sstohnij.stacktraceqabackendv0.email;

import com.sstohnij.stacktraceqabackendv0.email.template.EmailTemplateType;
import org.thymeleaf.context.Context;

public interface EmailSender {
    void sendEmail(String to, String subject, String message);
    void sendEmailUsingTemplate(String to, String subject, EmailTemplateType emailTemplateType, Context context);
}
