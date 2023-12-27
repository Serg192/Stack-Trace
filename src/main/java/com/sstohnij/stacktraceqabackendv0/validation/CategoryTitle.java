package com.sstohnij.stacktraceqabackendv0.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryTitleValidator.class)
public @interface CategoryTitle {
    String message() default "Category with such title is already present in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
