package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ServiceException;

import java.util.List;

public interface BookService {

    void createBook(String title, String authors, String genreName, String publisherName, int publishmentYear, int amount);

    List<Book> getAllBooks();

    Book getBookById(Integer id);

    void deleteBookById(Integer id);

    void updateBookById(Integer id, String title, String authors, String genreName, String publisherName, int publishmentYear, int amount) throws ServiceException;
}
