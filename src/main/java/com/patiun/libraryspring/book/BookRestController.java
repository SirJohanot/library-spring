package com.patiun.libraryspring.book;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRestController {

    private final BookService bookService;
    private final BookMapper mapper;

    @Autowired
    public BookRestController(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @PostMapping
    public void createBook(@RequestBody @Valid BookEditDto editDto) {
        Book inputBook = mapper.toBook(editDto);

        bookService.createBook(inputBook);
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
    public void updateBook(@PathVariable Integer id, @RequestBody @Valid BookEditDto editDto) {
        Book inputBook = mapper.toBook(editDto);

        bookService.updateBookById(id, inputBook);
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBookById(id);
    }

}
