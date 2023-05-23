package com.patiun.libraryspring.book;

import com.patiun.libraryspring.exception.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testCreateBookShouldSaveNewBookWhenSecondarySubjectsDoNotExist() {
        //given
        String title = "War and Peace";
        String authors = "Leo Tolstoy";
        String genreName = "Historical Novel";
        String publisherName = "Hardcover";
        Integer publishmentYear = 2014;
        Integer amount = 16;

        given(authorRepository.findByName(authors))
                .willReturn(Optional.empty());
        given(genreRepository.findByName(genreName))
                .willReturn(Optional.empty());
        given(publisherRepository.findByName(publisherName))
                .willReturn(Optional.empty());
        //when
        bookService.createBook(title, authors, genreName, publisherName, publishmentYear, amount);
        //then
        then(bookRepository)
                .should(times(1))
                .save(new Book(null, title, List.of(new Author(authors)), new Genre(null, genreName), new Publisher(null, publisherName), publishmentYear, amount, false));
    }

    @Test
    public void testCreateBookShouldSaveNewBookWithTheExistingSecondarySubjectsWhenSecondarySubjectsExist() {
        //given
        String title = "War and Peace";
        String authors = "Leo Tolstoy";
        String genreName = "Historical Novel";
        String publisherName = "Hardcover";
        Integer publishmentYear = 2014;
        Integer amount = 16;

        Author existingAuthor = new Author(3, authors);
        Genre existingGenre = new Genre(145, genreName);
        Publisher existingPublisher = new Publisher(35, publisherName);

        given(authorRepository.findByName(authors))
                .willReturn(Optional.of(existingAuthor));
        given(genreRepository.findByName(genreName))
                .willReturn(Optional.of(existingGenre));
        given(publisherRepository.findByName(publisherName))
                .willReturn(Optional.of(existingPublisher));
        //when
        bookService.createBook(title, authors, genreName, publisherName, publishmentYear, amount);
        //then
        then(bookRepository)
                .should(times(1))
                .save(new Book(null, title, List.of(existingAuthor), existingGenre, existingPublisher, publishmentYear, amount, false));
    }

    @Test
    public void testGetAllBooksShouldReturnAllNonDeletedBooksFoundByRepository() {
        //given
        Book firstBook = new Book(1, "book1", List.of(new Author(1, "author1")), new Genre(1, "genre1"), new Publisher(1, "publisher1"), 2003, 12, false);
        Book secondBook = new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false);
        Book thirdBook = new Book(3, "book3", Arrays.asList(new Author(1, "author1"), new Author(2, "author2")), new Genre(1, "genre1"), new Publisher(3, "publisher3"), 2014, 130, false);

        List<Book> booksFoundByRepository = Arrays.asList(firstBook, secondBook, thirdBook);

        given(bookRepository.findAllByIsDeletedFalse())
                .willReturn(booksFoundByRepository);
        //when
        List<Book> actualBooks = bookService.getAllBooks();
        //then
        assertThat(actualBooks)
                .isEqualTo(booksFoundByRepository);
    }

    @Test
    public void testGetBookByIdShouldReturnBookFoundByRepositoryWhenBookExists() {
        //given
        Integer targetBookId = 2;

        Book bookFoundByRepository = new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(bookFoundByRepository));
        //when
        Book actualBook = bookService.getBookById(targetBookId);
        //then
        assertThat(actualBook)
                .isEqualTo(bookFoundByRepository);
    }

    @Test
    public void testGetBookByIdShouldThrowElementNotFoundExceptionWhenBookDoesNotExist() {
        //given
        Integer targetBookId = 2;

        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookService.getBookById(targetBookId))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void testGetBookByIdShouldThrowElementNotFoundExceptionWhenBookIsDeleted() {
        //given
        Integer targetBookId = 2;

        Book expectedBook = new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, true);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(expectedBook));
        //then
        assertThatThrownBy(() -> bookService.getBookById(targetBookId))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void testDeleteBookByIdShouldSaveDeletedBookFoundByRepositoryWhenBookExists() {
        //given
        Integer targetBookId = 2;
        String title = "War and Peace";
        List<Author> authors = List.of(new Author(1, "Leo Tolstoy"));
        Genre genre = new Genre(1, "Historical Novel");
        Publisher publisher = new Publisher(2, "Hardcover");
        Integer publishmentYear = 2014;
        Integer amount = 16;

        Book existingBook = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, false);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(existingBook));

        Book expectedBookToBeSaved = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, true);
        //when
        bookService.deleteBookById(targetBookId);
        //then
        then(bookRepository)
                .should(times(1))
                .save(expectedBookToBeSaved);
    }

    @Test
    public void testDeleteBookByIdShouldThrowElementNotFoundExceptionWhenBookDoesNotExist() {
        //given
        Integer targetBookId = 2;

        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> bookService.deleteBookById(targetBookId))
                .isInstanceOf(ElementNotFoundException.class);

        then(bookRepository)
                .should(never())
                .save(any());
    }

    @Test
    public void testDeleteBookByIdShouldThrowElementNotFoundExceptionWhenBookDoesIsAlreadyDeleted() {
        //given
        Integer targetBookId = 2;
        String title = "War and Peace";
        List<Author> authors = List.of(new Author(1, "Leo Tolstoy"));
        Genre genre = new Genre(1, "Historical Novel");
        Publisher publisher = new Publisher(2, "Hardcover");
        Integer publishmentYear = 2014;
        Integer amount = 16;

        Book existingBook = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, true);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(existingBook));
        //then
        assertThatThrownBy(() -> bookService.deleteBookById(targetBookId))
                .isInstanceOf(ElementNotFoundException.class);

        then(bookRepository)
                .should(never())
                .save(any());
    }

    @Test
    public void testUpdateBookByIdShouldSaveNewBookWhenBookExists() {
        //given
        Integer targetBookId = 2;
        String title = "War and Peace";
        List<Author> authors = List.of(new Author(1, "Leo Tolstoy"));
        Genre genre = new Genre(1, "Historical Novel");
        Publisher publisher = new Publisher(2, "Hardcover");
        Integer publishmentYear = 2014;
        Integer amount = 16;

        Book existingBook = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, false);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(existingBook));

        String newTitle = "Peace and War";
        String newAuthors = "Tolstoy Leo";
        String newGenre = "Inverted Novel";
        Genre existingGenre = new Genre(3, newGenre);
        String newPublisher = "Some dude";
        Integer newPublishmentYear = 2020;
        Integer newAmount = 100;

        given(authorRepository.findByName(newAuthors))
                .willReturn(Optional.empty());
        given(genreRepository.findByName(newGenre))
                .willReturn(Optional.of(existingGenre));
        given(publisherRepository.findByName(newPublisher))
                .willReturn(Optional.empty());

        Book expectedBookToBeSaved = new Book(targetBookId, newTitle, List.of(new Author(newAuthors)), existingGenre, new Publisher(null, newPublisher), newPublishmentYear, newAmount, false);
        //when
        bookService.updateBookById(targetBookId, newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);
        //then
        then(bookRepository)
                .should(times(1))
                .save(expectedBookToBeSaved);
    }

    @Test
    public void testUpdateBookByIdShouldThrowElementNotFoundExceptionWhenBookDoesNotExist() {
        //given
        Integer targetBookId = 2;

        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.empty());

        String newTitle = "Peace and War";
        String newAuthors = "Tolstoy Leo";
        String newGenre = "Inverted Novel";
        String newPublisher = "Some dude";
        Integer newPublishmentYear = 2020;
        Integer newAmount = 100;
        //then
        assertThatThrownBy(() -> bookService.updateBookById(targetBookId, newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount))
                .isInstanceOf(ElementNotFoundException.class);

        then(bookRepository)
                .should(never())
                .save(any());
    }

    @Test
    public void testUpdateBookByIdShouldThrowElementNotFoundExceptionWhenBookDoesIsDeleted() {
        //given
        Integer targetBookId = 2;
        String title = "War and Peace";
        List<Author> authors = List.of(new Author(1, "Leo Tolstoy"));
        Genre genre = new Genre(1, "Historical Novel");
        Publisher publisher = new Publisher(2, "Hardcover");
        Integer publishmentYear = 2014;
        Integer amount = 16;

        Book existingBook = new Book(targetBookId, title, authors, genre, publisher, publishmentYear, amount, true);
        given(bookRepository.findById(targetBookId))
                .willReturn(Optional.of(existingBook));

        String newTitle = "Peace and War";
        String newAuthors = "Tolstoy Leo";
        String newGenre = "Inverted Novel";
        String newPublisher = "Some dude";
        Integer newPublishmentYear = 2020;
        Integer newAmount = 100;
        //then
        assertThatThrownBy(() -> bookService.updateBookById(targetBookId, newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount))
                .isInstanceOf(ElementNotFoundException.class);

        then(bookRepository)
                .should(never())
                .save(any());
    }

}
