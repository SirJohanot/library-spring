package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.utility.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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

    @GetMapping({"/books/", "/books/{page}"})
    public String books(@PathVariable(name = "page") Optional<Integer> optionalPage, final Model model) {
        int page = optionalPage.orElse(1);

        List<Book> books = bookService.getAllBooks();

        List<Book> booksOfTheTargetPage = bookPaginator.getEntitiesOfPage(books, page, BOOKS_PER_PAGE);
        int pagesNeededToContainAllBooks = bookPaginator.getNumberOfPagesToContainEntities(books, BOOKS_PER_PAGE);
        int acceptableTargetPage = bookPaginator.getClosestAcceptableTargetPage(books, page, BOOKS_PER_PAGE);

        model.addAttribute("books", booksOfTheTargetPage);
        model.addAttribute("currentPage", acceptableTargetPage);
        model.addAttribute("maxPage", pagesNeededToContainAllBooks);

        return "books";
    }

    @GetMapping("/book/{id}")
    public String book(@PathVariable Integer id, final Model model) throws ServiceException {
        getBookByIdAndAddToModel(id, model);

        return "book";
    }

    @PostMapping("/delete-book")
    public String deleteBook(@RequestParam Integer id) throws ServiceException {
        bookService.deleteBookById(id);
        return "redirect:/books/";
    }

    @GetMapping("/edit-book-page/{id}")
    public String editBookPage(@PathVariable Integer id, final Model model) throws ServiceException {
        getBookByIdAndAddToModel(id, model);

        return "editBook";
    }

    @PostMapping("/edit-book")
    public String editBook(@RequestParam Integer id, @RequestParam String title, @RequestParam String authors, @RequestParam String genre, @RequestParam String publisher, @RequestParam("publishment-year") Integer publishmentYear, @RequestParam Integer amount) throws ServiceException {
        bookService.updateBookById(id, title, authors, genre, publisher, publishmentYear, amount);
        return "redirect:/book/" + id;
    }

    private void getBookByIdAndAddToModel(Integer id, final Model model) throws ServiceException {
        Book book = bookService.getBookById(id);

        model.addAttribute("book", book);
    }

}
