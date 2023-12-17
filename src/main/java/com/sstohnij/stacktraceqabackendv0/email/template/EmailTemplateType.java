package com.sstohnij.stacktraceqabackendv0.email.template;

import org.thymeleaf.context.Context;

public enum EmailTemplateType {
    EMAIL_CONFIRMATION("email-pattern-confirmation.html");

    private final String templatePath;

    EmailTemplateType(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplate() {
        return templatePath;
    }

    public Context getDefaultTemplateContext() {
        return switch (this) {
            case EMAIL_CONFIRMATION -> getEmailConfirmationContext();
        };
    }
    private Context getEmailConfirmationContext(){
        Context context = new Context();
        context.setVariable("title", "Email confirmation");
        return context;
    }

}
