package com.patiun.libraryspring.book;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EditorRepository extends CrudRepository<Editor, Integer> {

    Optional<Editor> findByRoleAndName(String role, String name);
}
