package com.patiun.libraryspring.book;

import java.util.List;

public interface BookService {

    void createBook(String title, List<String> authors, String genreName, String publisherName, int publishmentYear, int amount);

    List<Book> getAllBooks();

    Book getBookById(Integer id);

    void deleteBookById(Integer id);

    void updateBookById(Integer id, String title, List<String> authors, String genreName, String publisherName, int publishmentYear, int amount);
}
