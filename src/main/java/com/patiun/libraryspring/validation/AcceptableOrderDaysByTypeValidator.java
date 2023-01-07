package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.order.BookOrderDto;
import com.patiun.libraryspring.order.RentalType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class AcceptableOrderDaysByTypeValidator implements ConstraintValidator<AcceptableOrderDaysByType, BookOrderDto> {

    private RentalType targetType;
    private List<Integer> acceptableIntegers;

    @Override
    public void initialize(AcceptableOrderDaysByType constraintAnnotation) {
        targetType = constraintAnnotation.type();

        int[] acceptableIntegersArray = constraintAnnotation.value();
        acceptableIntegers = Arrays.stream(acceptableIntegersArray)
                .boxed()
                .toList();
    }

    @Override
    public boolean isValid(BookOrderDto value, ConstraintValidatorContext context) {
        RentalType valueType = value.getRentalType();
        Integer valueDays = value.getDays();
        return (targetType != valueType || acceptableIntegers.contains(valueDays));
    }
}
