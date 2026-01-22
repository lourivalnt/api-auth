package com.auth.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.auth.validation.validator.StrongPasswordValidator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface StrongPassword {

    String message() default 
        "A senha deve conter no mínimo 8 caracteres, letra maiúscula, minúscula, número e caractere especial";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
