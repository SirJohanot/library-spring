package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    public void addBook(@RequestBody @Valid BookEditDto editDto) {
        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.createBook(title, authors, genre, publisher, publishmentYear, amount);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable Integer id, @RequestBody @Valid BookEditDto editDto) throws ServiceException {
        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.updateBookById(id, title, authors, genre, publisher, publishmentYear, amount);
    }

}