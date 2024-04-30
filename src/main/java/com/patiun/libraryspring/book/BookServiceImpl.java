package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

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
    public void createBook(Book inputBook) {
        Book newBook = setupBookToPersist(inputBook);
        bookRepository.save(newBook);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAllByIsDeletedFalse();
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
    public void updateBookById(Integer id, Book inputBook) {
        getExistingBookById(id);
        inputBook.setId(id);
        Book updatedBook = setupBookToPersist(inputBook);
        bookRepository.save(updatedBook);
    }

    private Book setupBookToPersist(Book inputBook) {
        List<Author> authors = inputBook.getAuthors()
                .stream()
                .map(author -> {
                    String authorName = author.getName();
                    Optional<Author> existingAuthorOptional = authorRepository.findByName(authorName);
                    return existingAuthorOptional.orElse(author);
                })
                .toList();
        String genreName = inputBook.getGenre()
                .getName();
        Genre genre = genreRepository.findByName(genreName)
                .orElse(new Genre(null, genreName));

        String publisherName = inputBook.getPublisher()
                .getName();
        Publisher publisher = publisherRepository.findByName(publisherName)
                .orElse(new Publisher(null, publisherName));

        Integer id = inputBook.getId();
        String title = inputBook.getTitle();
        int publishmentYear = inputBook.getPublishmentYear();
        String publishmentLocation = inputBook.getPublishmentLocation();
        String isbn = inputBook.getIsbn();
        int amount = inputBook.getAmount();

        return new Book(id, title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount, false);
    }

    private Book getExistingBookById(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty() || bookOptional.get().isDeleted()) {
            throw new ElementNotFoundException("Could not find a book by id = " + id);
        }
        return bookOptional.get();
    }

}
