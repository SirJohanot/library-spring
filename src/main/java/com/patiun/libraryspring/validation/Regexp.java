package com.patiun.libraryspring.validation;

public interface Regexp {

    String HUMAN_NAME = "\\p{L}+.*";

    String HUMAN_NAMES_DELIMITED_BY_COMMA = "\\p{L}+.*( *, +\\p{L}+.*)*";

    String WORD = "[\\p{L}\\w]+.*";

}
