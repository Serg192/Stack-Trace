package com.sstohnij.stacktraceqabackendv0.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostCategorySetValidator.class)
public @interface PostCategorySet {
    String message() default "Category set contains invalid categories which are not present in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
