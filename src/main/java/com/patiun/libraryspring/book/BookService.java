package com.patiun.libraryspring.book;

import java.util.List;

public interface BookService {

    void createBook(Book inputBook);

    List<Book> getAllBooks();

    Book getBookById(Integer id);

    void deleteBookById(Integer id);
    
    void updateBookById(Integer id, Book inputBook);
}
