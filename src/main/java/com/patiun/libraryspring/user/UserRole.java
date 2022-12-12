package com.patiun.libraryspring.user;

public enum UserRole {

    READER,
    LIBRARIAN,
    ADMIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
