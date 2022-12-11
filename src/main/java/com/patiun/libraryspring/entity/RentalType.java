package com.patiun.libraryspring.entity;

public enum RentalType {

    TO_READING_HALL,
    OUT_OF_LIBRARY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
