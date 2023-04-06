package com.patiun.libraryspring.book;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findAllByIsDeletedFalse();
}
