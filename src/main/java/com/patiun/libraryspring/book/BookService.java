package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ServiceException;
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

    public Book getBookById(Integer id) throws ServiceException {
        return getExistingBookById(id);
    }

    public void deleteBookById(Integer id) throws ServiceException {
        Book targetBook = getExistingBookById(id);
        targetBook.setDeleted(true);
        bookRepository.save(targetBook);
    }

    public void updateBookById(Integer id, String title, String authors, String genreName, String publisherName, Integer publishmentYear, Integer amount) throws ServiceException {
        getExistingBookById(id);
        List<Author> authorList = authorsStringToList(authors);
        Book updatedBook = setupBookToPersist(id, title, authorList, genreName, publisherName, publishmentYear, amount);
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

    private Book getExistingBookById(Integer id) throws ServiceException {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new ServiceException("Could not find a book by id = " + id);
        }
        return bookOptional.get();
    }

}
