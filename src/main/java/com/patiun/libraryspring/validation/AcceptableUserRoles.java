package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.user.UserRole;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptableUserRolesValidator.class)
@Documented
public @interface AcceptableUserRoles {

    String message() default "Passwords must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    UserRole[] value();
}
