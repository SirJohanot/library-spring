package com.patiun.libraryspring.validation;

public final class Regexp {

    private Regexp() {
    }

    public static final String HUMAN_NAME = "\\p{L}+.*";

    public static final String HUMAN_NAMES_DELIMITED_BY_COMMA = "\\p{L}+.*( *, +\\p{L}+.*)*";

    public static final String WORD = "[\\p{L}\\w]+.*";

}
