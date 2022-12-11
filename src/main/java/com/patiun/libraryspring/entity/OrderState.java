package com.patiun.libraryspring.entity;

public enum OrderState {

    PLACED,
    APPROVED,
    DECLINED,
    BOOK_TAKEN,
    BOOK_RETURNED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
