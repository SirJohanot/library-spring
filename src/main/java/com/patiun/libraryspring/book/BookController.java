package com.patiun.libraryspring.book;

import com.patiun.libraryspring.utility.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class BookController {

    private static final Integer BOOKS_PER_PAGE = 5;

    private final BookService bookService;
    private final Paginator<Book> bookPaginator;

    @Autowired
    public BookController(BookService bookService, Paginator<Book> bookPaginator) {
        this.bookService = bookService;
        this.bookPaginator = bookPaginator;
    }

    @PostMapping("/add-book")
    public String addBook(@RequestParam String title, @RequestParam String authors, @RequestParam String genre, @RequestParam String publisher, @RequestParam("publishment-year") Integer publishmentYear, @RequestParam Integer amount) {
        bookService.createBook(title, authors, genre, publisher, publishmentYear, amount);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String books(@RequestParam(defaultValue = "1") Integer page, final Model model) {
        List<Book> books = bookService.getAllBooks();

        List<Book> booksOfTheTargetPage = bookPaginator.getEntitiesOfPage(books, page, BOOKS_PER_PAGE);
        int pagesNeededToContainAllBooks = bookPaginator.getNumberOfPagesToContainEntities(books, BOOKS_PER_PAGE);
        int acceptableTargetPage = bookPaginator.getClosestAcceptableTargetPage(books, page, BOOKS_PER_PAGE);

        model.addAttribute("books", booksOfTheTargetPage);
        model.addAttribute("currentPage", acceptableTargetPage);
        model.addAttribute("maxPage", pagesNeededToContainAllBooks);

        return "books";
    }

    @GetMapping("/book")
    public String book(@RequestParam Integer id, final Model model) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isEmpty()) {
            throw new NoSuchElementException("Could not find a book by id = " + id);
        }
        Book book = bookOptional.get();
        
        model.addAttribute("book", book);

        return "book";
    }
}
