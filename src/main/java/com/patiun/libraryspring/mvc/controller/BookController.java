package com.patiun.libraryspring.mvc.controller;

import com.patiun.libraryspring.book.Book;
import com.patiun.libraryspring.book.BookEditDto;
import com.patiun.libraryspring.book.BookService;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.order.BookOrderDto;
import com.patiun.libraryspring.utility.Paginator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "true")
public class BookController {

    private static final Integer BOOKS_PER_PAGE = 5;

    private final BookService bookService;
    private final Paginator<Book> bookPaginator;

    @Autowired
    public BookController(BookService bookService, Paginator<Book> bookPaginator) {
        this.bookService = bookService;
        this.bookPaginator = bookPaginator;
    }

    @GetMapping("/add-book")
    public String addBookPage(final Model model) {
        BookEditDto editDto = new BookEditDto();

        model.addAttribute("editDto", editDto);

        return "addABook";
    }

    @PostMapping("/add-book")
    public String addBook(@ModelAttribute("editDto") @Valid BookEditDto editDto, final BindingResult result, final Model model) {
        if (result.hasErrors()) {
            bindErrorToModelAttribute(result, model);
            return addBookPage(model);
        }
        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.createBook(title, authors, genre, publisher, publishmentYear, amount);
        return "redirect:/books/";
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
        Book book = bookService.getBookById(id);
        BookOrderDto orderDto = new BookOrderDto();

        model.addAttribute("book", book);
        model.addAttribute("orderDto", orderDto);

        return "book";
    }

    @PostMapping("/delete-book")
    public String deleteBook(@RequestParam Integer id) {
        bookService.deleteBookById(id);
        return "redirect:/books/";
    }

    @GetMapping("/edit-book/{id}")
    public String editBookPage(@PathVariable Integer id, final Model model) {
        Book book = bookService.getBookById(id);

        BookEditDto editDto = new BookEditDto(book);
        model.addAttribute("editDto", editDto);

        return "editBook";
    }

    @PostMapping("/edit-book")
    public String editBook(@RequestParam Integer id, @ModelAttribute("editDto") @Valid BookEditDto editDto, final BindingResult result, final Model model) throws ServiceException {
        if (result.hasErrors()) {
            bindErrorToModelAttribute(result, model);
            return editBookPage(id, model);
        }

        String title = editDto.getTitle();
        String authors = editDto.getAuthors();
        String genre = editDto.getGenre();
        String publisher = editDto.getPublisher();
        Integer publishmentYear = editDto.getPublishmentYear();
        Integer amount = editDto.getAmount();

        bookService.updateBookById(id, title, authors, genre, publisher, publishmentYear, amount);
        return "redirect:/book/" + id;
    }

    private void bindErrorToModelAttribute(final BindingResult result, final Model model) {
        List<ObjectError> allErrors = result.getAllErrors();
        ObjectError firstError = allErrors.get(0);
        String errorMessage = firstError.getDefaultMessage();
        model.addAttribute("error", errorMessage);
    }

}
