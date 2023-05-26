package com.patiun.libraryspring.validation;

import com.patiun.libraryspring.user.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordsMatchValidatorTest {

    private PasswordsMatchValidator validator;

    @BeforeEach
    public void setup() {
        validator = new PasswordsMatchValidator();
    }

    @Test
    public void testIsValidShouldReturnTrueWhenPasswordsMatch() {
        //given
        UserRegistrationDto user = new UserRegistrationDto("login", "password", "password", "firstName", "lastName");
        //when
        boolean isValid = validator.isValid(user, null);
        //then
        assertThat(isValid)
                .isTrue();
    }

    @Test
    public void testIsValidShouldReturnFalseWhenPasswordsDoNotMatch() {
        //given
        UserRegistrationDto user = new UserRegistrationDto("login", "password", "differentPassword", "firstName", "lastName");
        //when
        boolean isValid = validator.isValid(user, null);
        //then
        assertThat(isValid)
                .isFalse();
    }
}
