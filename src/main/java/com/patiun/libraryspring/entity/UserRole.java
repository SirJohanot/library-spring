package com.patiun.libraryspring.entity;

public enum UserRole {

    READER,
    LIBRARIAN,
    ADMIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
