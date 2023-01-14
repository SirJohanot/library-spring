package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ElementNotFoundException;
import com.patiun.libraryspring.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private static final String AUTHORS_DELIMITER_REGEX = " *, *";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void createBook(String title, String authors, String genreName, String publisherName, Integer publishmentYear, Integer amount) {
        List<Author> authorList = authorsStringToList(authors);
        Book newBook = setupBookToPersist(null, title, authorList, genreName, publisherName, publishmentYear, amount);
        bookRepository.save(newBook);
    }

    @Override
    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Book getBookById(Integer id) {
        return getExistingBookById(id);
    }

    @Override
    public void deleteBookById(Integer id) {
        Book targetBook = getExistingBookById(id);
        targetBook.setDeleted(true);
        bookRepository.save(targetBook);
    }

    @Override
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

        Genre genre = genreRepository.findOptionalByName(genreName)
                .orElse(new Genre(null, genreName));

        Publisher publisher = publisherRepository.findOptionalByName(publisherName)
                .orElse(new Publisher(null, publisherName));

        return new Book(id, title, authors, genre, publisher, publishmentYear, amount, false);
    }

    private Book getExistingBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Could not find a book by id = " + id));
    }

}
