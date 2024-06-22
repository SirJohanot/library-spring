package com.patiun.libraryspring.user;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class NewPasswordDto {

    @NotBlank(message = "Password must not be blank")
    private String password;

    public NewPasswordDto() {
    }

    public NewPasswordDto(String password) {
        this.password = password;
    }

    public @NotBlank(message = "Password must not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password must not be blank") String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NewPasswordDto that = (NewPasswordDto) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

    @Override
    public String toString() {
        return "NewPasswordDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
