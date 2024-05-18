package com.patiun.libraryspring.validation;

public final class Regexp {

    private Regexp() {
    }

    public static final String HUMAN_NAME = "\\p{L}+.*";

    public static final String WORD = "[\\p{L}\\w]+.*";

    public static final String ISBN = "^([0-9]{10}|[0-9]{13})$";

    public static final String UDC_BBC = "^\\d+.*";

    public static final String AUTHOR_INDEX = "^\\p{L}\\-?\\d+$";

}
