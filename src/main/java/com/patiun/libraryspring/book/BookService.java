package com.patiun.libraryspring.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    private static final String AUTHORS_DELIMITER_REGEX = " *, *";

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void createBook(String title, String authors, String genre, String publisher, Integer publishmentYear, Integer amount) {
        String[] splitAuthors = authors.split(AUTHORS_DELIMITER_REGEX);
        List<Author> authorList = Arrays.stream(splitAuthors)
                .map(Author::new)
                .toList();
        Genre bookGenre = new Genre(null, genre);
        Publisher bookPublisher = new Publisher(null, publisher);
        Book newBook = new Book(null, title, authorList, bookGenre, bookPublisher, publishmentYear, amount, false);
        bookRepository.save(newBook);
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .toList();
    }
}
