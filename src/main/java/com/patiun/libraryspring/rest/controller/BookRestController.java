package com.patiun.libraryspring.rest.controller;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.book.BookEditDto;
import com.patiun.libraryspring.book.BookService;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.rest.configuration.RestSecurityConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = RestSecurityConfig.FRONT_END_URL)
@RequestMapping("/books")
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true)
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public void createBook(@RequestBody @Valid BookEditDto editDto) {
        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.createBook(title, authors, genre, publisher, publishmentYear, amount);
    }

    @GetMapping
    public List<Book> readAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("{id}")
    public Book readBook(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @PutMapping("{id}")
    public void updateBook(@PathVariable Integer id, @RequestBody @Valid BookEditDto editDto) throws ServiceException {
        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.updateBookById(id, title, authors, genre, publisher, publishmentYear, amount);
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBookById(id);
    }

}
