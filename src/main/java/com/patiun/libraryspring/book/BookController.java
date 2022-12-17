package com.patiun.libraryspring.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add-book")
    public String addBook(@RequestParam String title, @RequestParam String authors, @RequestParam String genre, @RequestParam String publisher, @RequestParam("publishment-year") Integer publishmentYear, @RequestParam Integer amount) {
        bookService.createBook(title, authors, genre, publisher, publishmentYear, amount);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String books(@RequestParam(defaultValue = "1") Integer page, final Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", 10);
        return "books";
    }
}
