package com.patiun.libraryspring.book;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Integer> {

    Optional<Genre> findOptionalByName(String name);
}
