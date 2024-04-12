package com.patiun.libraryspring.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static com.patiun.libraryspring.user.Authority.*;

public enum UserRole {

    READER(READ_BOOKS, READ_ORDERS, PLACE_ORDERS),
    LIBRARIAN(READ_BOOKS, READ_ORDERS, JUDGE_ORDERS, COLLECT_ORDERS),
    ADMIN(READ_BOOKS, EDIT_BOOKS, ADD_BOOKS, READ_USERS, EDIT_USERS);

    private final Authority[] authorities;

    UserRole(Authority... authorities) {
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Stream<String> roleString = Stream.of("ROLE_" + name());
        Stream<String> authoritiesString = Arrays.stream(authorities)
                .map(Enum::name);
        return Stream.concat(roleString, authoritiesString)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

}
