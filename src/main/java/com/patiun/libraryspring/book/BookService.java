package com.patiun.libraryspring.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    private static final String AUTHORS_DELIMITER_REGEX = " *, *";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }

    public void createBook(String title, String authors, String genreName, String publisherName, Integer publishmentYear, Integer amount) {
        List<Author> authorList = authorsStringToList(authors);
        Book newBook = setupBookToPersist(null, title, authorList, genreName, publisherName, publishmentYear, amount);
        bookRepository.save(newBook);
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .toList();
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public void deleteBookById(Integer id) {
        bookRepository.deleteById(id);
    }

    public void updateBookById(Integer id, String title, String authors, String genreName, String publisherName, Integer publishmentYear, Integer amount) {
        List<Author> authorList = authorsStringToList(authors);
        Book updatedBook = setupBookToPersist(id, title, authorList, genreName, publisherName, publishmentYear, amount);
        System.out.println(updatedBook);
        bookRepository.save(updatedBook);
    }

    private List<Author> authorsStringToList(String authors) {
        String[] splitAuthors = authors.split(AUTHORS_DELIMITER_REGEX);
        return Arrays.stream(splitAuthors)
                .map(Author::new)
                .toList();
    }

    private Book setupBookToPersist(Integer id, String title, List<Author> authors, String genreName, String publisherName, Integer publishmentYear, Integer amount) {
        authors = authors.stream()
                .map(author -> {
                    String authorName = author.getName();
                    Optional<Author> existingAuthorOptional = authorRepository.findOptionalByName(authorName);
                    return existingAuthorOptional.orElse(author);
                })
                .toList();

        Optional<Genre> existingGenreOptional = genreRepository.findOptionalByName(genreName);
        Genre genre = existingGenreOptional.orElse(new Genre(null, genreName));

        Optional<Publisher> existingPublisherOptional = publisherRepository.findOptionalByName(publisherName);
        Publisher publisher = existingPublisherOptional.orElse(new Publisher(null, publisherName));

        return new Book(id, title, authors, genre, publisher, publishmentYear, amount, false);
    }
}
