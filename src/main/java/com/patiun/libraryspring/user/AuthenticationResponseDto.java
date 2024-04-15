package com.patiun.libraryspring.user;

import java.util.Arrays;
import java.util.Objects;

public class AuthenticationResponseDto {

    private String accessToken;

    private UserRole[] roles;

    public AuthenticationResponseDto() {
    }

    public AuthenticationResponseDto(String accessToken, UserRole[] roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserRole[] getRoles() {
        return roles;
    }

    public void setRoles(UserRole[] roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthenticationResponseDto that = (AuthenticationResponseDto) o;
        return Objects.equals(accessToken, that.accessToken) && Arrays.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Arrays.hashCode(roles);
        return result;
    }

    @Override
    public String toString() {
        return "AuthenticationResponseDto{" +
                "accessToken='" + accessToken + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
