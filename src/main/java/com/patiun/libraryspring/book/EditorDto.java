package com.patiun.libraryspring.book;

import com.patiun.libraryspring.validation.Regexp;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class EditorDto {

    @Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor role must start with an alphabetical character")
    private String role;

    @Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor name must start with an alphabetical character")
    private String name;

    public EditorDto() {
    }

    public EditorDto(String role, String name) {
        this.role = role;
        this.name = name;
    }

    public @Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor role must start with an alphabetical character") String getRole() {
        return role;
    }

    public void setRole(@Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor role must start with an alphabetical character") String role) {
        this.role = role;
    }

    public @Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor name must start with an alphabetical character") String getName() {
        return name;
    }

    public void setName(@Pattern(regexp = Regexp.HUMAN_NAME, message = "Editor name must start with an alphabetical character") String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EditorDto editorDto = (EditorDto) o;
        return Objects.equals(role, editorDto.role) && Objects.equals(name, editorDto.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }

    @Override
    public String toString() {
        return "EditorDto{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
