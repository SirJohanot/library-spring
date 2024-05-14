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
    private final EditorRepository editorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final PrintingHouseRepository printingHouseRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, EditorRepository editorRepository, GenreRepository genreRepository, PublisherRepository publisherRepository, PrintingHouseRepository printingHouseRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.editorRepository = editorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.printingHouseRepository = printingHouseRepository;
    }

    @Override
    public void createBook(Book inputBook) {
        setupBookToPersist(inputBook);
        bookRepository.save(inputBook);
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
        setupBookToPersist(inputBook);
        bookRepository.save(inputBook);
    }

    private void setupBookToPersist(Book inputBook) {
        List<Author> authors = inputBook.getAuthors()
                .stream()
                .map(author -> {
                    String authorName = author.getName();
                    Optional<Author> existingAuthorOptional = authorRepository.findByName(authorName);
                    return existingAuthorOptional.orElse(author);
                })
                .toList();
        inputBook.setAuthors(authors);

        List<Editor> editors = inputBook.getEditors()
                .stream()
                .map(editor -> {
                    String editorRole = editor.getRole();
                    String editorName = editor.getName();
                    Optional<Editor> existingEditorOptional = editorRepository.findByRoleAndName(editorRole, editorName);
                    return existingEditorOptional.orElse(editor);
                })
                .toList();
        inputBook.setEditors(editors);

        String genreName = inputBook.getGenre()
                .getName();
        Genre genre = genreRepository.findByName(genreName)
                .orElse(new Genre(null, genreName));
        inputBook.setGenre(genre);

        String publisherName = inputBook.getPublisher()
                .getName();
        String publisherPostalCode = inputBook.getPublisher()
                .getPostalCode();
        String publisherAddress = inputBook.getPublisher()
                .getAddress();
        Publisher publisher = publisherRepository.findByNameAndPostalCodeAndAddress(publisherName, publisherPostalCode, publisherAddress)
                .orElse(new Publisher(null, publisherName, publisherPostalCode, publisherAddress));
        inputBook.setPublisher(publisher);

        String printingHouseName = inputBook.getPrintingHouse()
                .getName();
        String printingHousePostalCode = inputBook.getPrintingHouse()
                .getPostalCode();
        String printingHouseAddress = inputBook.getPrintingHouse()
                .getAddress();
        PrintingHouse printingHouse = printingHouseRepository.findByNameAndPostalCodeAndAddress(printingHouseName, printingHousePostalCode, printingHouseAddress)
                .orElse(new PrintingHouse(null, printingHouseName, printingHousePostalCode, printingHouseAddress));
        inputBook.setPrintingHouse(printingHouse);
    }

    private Book getExistingBookById(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty() || bookOptional.get().isDeleted()) {
            throw new ElementNotFoundException("Could not find a book by id = " + id);
        }
        return bookOptional.get();
    }

}
