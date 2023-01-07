package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.user.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class AcceptableRolesValidator implements ConstraintValidator<AcceptableRoles, UserRole> {

    private List<UserRole> acceptableRoles;

    @Override
    public void initialize(AcceptableRoles constraintAnnotation) {
        UserRole[] acceptableRolesArray = constraintAnnotation.value();
        acceptableRoles = List.of(acceptableRolesArray);
    }

    @Override
    public boolean isValid(UserRole value, ConstraintValidatorContext context) {
        return acceptableRoles.contains(value);
    }
}
