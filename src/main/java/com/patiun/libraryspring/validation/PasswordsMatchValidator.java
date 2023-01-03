package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.user.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDto> {

    @Override
    public boolean isValid(UserRegistrationDto value, ConstraintValidatorContext context) {
        String password = value.getPassword();
        String confirmedPassword = value.getConfirmedPassword();
        return password.equals(confirmedPassword);
    }
}
