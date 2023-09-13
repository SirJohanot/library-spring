package com.patiun.libraryspring.user;

import com.patiun.libraryspring.validation.AcceptableRoles;
import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class UserEditDto {

    @Pattern(regexp = Regexp.HUMAN_NAME, message = "First name must start with an alphabetical character")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Pattern(regexp = Regexp.HUMAN_NAME, message = "Last name must start with an alphabetical character")
    private String lastName;

    @AcceptableRoles({UserRole.READER, UserRole.LIBRARIAN})
    private UserRole role;

    public UserEditDto(String firstName, String lastName, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEditDto that = (UserEditDto) o;

        if (!Objects.equals(firstName, that.firstName)) {
            return false;
        }
        if (!Objects.equals(lastName, that.lastName)) {
            return false;
        }
        return role == that.role;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserEditDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }
}
