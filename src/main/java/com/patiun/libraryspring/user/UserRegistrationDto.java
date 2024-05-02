package com.patiun.libraryspring.user;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class UserRegistrationDto {

    @NotBlank(message = "Login must not be blank")
    private String login;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotNull(message = "First name must not be null")
    @Pattern(regexp = Regexp.HUMAN_NAME, message = "First name must start with an alphabetical character")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @Pattern(regexp = Regexp.HUMAN_NAME, message = "Last name must start with an alphabetical character")
    private String lastName;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public @NotBlank(message = "Login must not be blank") String getLogin() {
        return login;
    }

    public void setLogin(@NotBlank(message = "Login must not be blank") String login) {
        this.login = login;
    }

    public @NotBlank(message = "Password must not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password must not be blank") String password) {
        this.password = password;
    }

    public @NotNull(message = "First name must not be null") @Pattern(regexp = Regexp.HUMAN_NAME, message = "First name must start with an alphabetical character") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull(message = "First name must not be null") @Pattern(regexp = Regexp.HUMAN_NAME, message = "First name must start with an alphabetical character") String firstName) {
        this.firstName = firstName;
    }

    public @NotNull(message = "Last name must not be null") @Pattern(regexp = Regexp.HUMAN_NAME, message = "Last name must start with an alphabetical character") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull(message = "Last name must not be null") @Pattern(regexp = Regexp.HUMAN_NAME, message = "Last name must start with an alphabetical character") String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRegistrationDto that = (UserRegistrationDto) o;

        if (!Objects.equals(login, that.login)) {
            return false;
        }
        if (!Objects.equals(password, that.password)) {
            return false;
        }
        if (!Objects.equals(firstName, that.firstName)) {
            return false;
        }
        return Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
