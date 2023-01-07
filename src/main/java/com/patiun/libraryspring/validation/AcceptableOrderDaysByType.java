package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.order.RentalType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcceptableOrderDaysByTypeValidator.class)
@Repeatable(AcceptableOrderDaysByTypeContainer.class)
@Documented
public @interface AcceptableOrderDaysByType {

    String message() default "The value is unacceptable";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] value();

    RentalType type();
}
