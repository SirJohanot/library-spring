package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.user.UserRole;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptableRolesValidator.class)
@Documented
public @interface AcceptableRoles {

    String message() default "The user role is unacceptable";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    UserRole[] value();
}
