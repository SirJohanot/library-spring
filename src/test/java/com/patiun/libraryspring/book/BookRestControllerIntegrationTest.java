package com.patiun.libraryspring.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.patiun.libraryspring.utility.TestUtilities.asJsonString;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class BookRestControllerIntegrationTest {

    private static final String BASE_URL = "/books";

    private static final String DUMMY_ADMIN_CREDENTIALS = "admin";

    private final MockMvc mvc;

    private final BookRepository bookRepository;

    private final TestEntityManager testEntityManager;

    @Autowired
    public BookRestControllerIntegrationTest(MockMvc mvc, BookRepository bookRepository, TestEntityManager testEntityManager) {
        this.mvc = mvc;
        this.bookRepository = bookRepository;
        this.testEntityManager = testEntityManager;
    }

    @Test
    public void testCreateBookShouldSaveTheNewBookToTheDatabase() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertThat(bookRepository.findAll())
                .hasSize(1)
                .allMatch(b -> Objects.equals(b.getTitle(), title) &&
                        Objects.equals(b.getAuthors().stream()
                                .map(Author::getName)
                                .collect(Collectors.toList()), authors) &&
                        Objects.equals(b.getGenre().getName(), genre) &&
                        Objects.equals(b.getPublisher().getName(), publisher) &&
                        Objects.equals(b.getPublishmentYear(), publishmentYear) &&
                        Objects.equals(b.getAmount(), amount)
                );
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookTitleIsBlank() throws Exception {
        //given
        String title = "";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookTitleIsNotAWord() throws Exception {
        //given
        String title = "'%$%^'";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookAuthorsIsEmpty() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = new ArrayList<>();
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookAuthorsAreNotHumanNames() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("&^%*&$", "&*^^%");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookGenreIsBlank() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookGenreIsNotAWord() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "%genre@#";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookPublisherIsBlank() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookPublisherIsNotAWord() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "*%^^%346534hjbdg";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookPublishmentYearIsEarlierThan1900() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 1899;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookPublishmentYearIsLaterThan2500() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2525;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testCreateBookShouldReturnBadRequestWhenTheBookAmountIsANegativeNumber() throws Exception {
        //given
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2023;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = -144;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testReadAllBooksShouldReturnTheBookListOfUndeletedBooksWhenNoExistingBooksAreDeleted() throws Exception {
        //given
        List<Book> existingBooksOnTheDatabase = Arrays.asList(
                new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false),
                new Book(null, "book2", List.of(new Author(null, "author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, "Moscow", "8461945178395", 7, false),
                new Book(null, "book3", Arrays.asList(new Author(null, "author3"), new Author(null, "author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, "Moscow", "8461945178395", 130, false)
        );

        existingBooksOnTheDatabase = existingBooksOnTheDatabase.stream()
                .map(testEntityManager::persist)
                .toList();

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = existingBooksOnTheDatabase.stream()
                .map(u -> {
                    try {
                        return mapper.writeValueAsString(u);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));
        expectedJson = "[" + expectedJson + "]";
        //then
        mvc.perform(get(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReadAllBooksShouldReturnTheBookListOfUndeletedBooksWhenSomeExistingBooksAreDeleted() throws Exception {
        //given
        List<Book> existingBooksOnTheDatabase = Arrays.asList(
                new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, true),
                new Book(null, "book2", List.of(new Author(null, "author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, "Moscow", "8461945178395", 7, true),
                new Book(null, "book3", Arrays.asList(new Author(null, "author3"), new Author(null, "author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, "Moscow", "8461945178395", 130, false),
                new Book(null, "book4", List.of(new Author(null, "author5")), new Genre(null, "genre4"), new Publisher(null, "publisher4"), 1995, "Moscow", "8461945178395", 201, true),
                new Book(null, "book5", List.of(new Author(null, "author6")), new Genre(null, "genre5"), new Publisher(null, "publisher5"), 2012, "Moscow", "8461945178395", 12, false)
        );

        existingBooksOnTheDatabase = existingBooksOnTheDatabase.stream()
                .map(testEntityManager::persist)
                .toList();

        List<Book> expectedBooks = existingBooksOnTheDatabase.stream()
                .filter(not(Book::isDeleted))
                .toList();

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = expectedBooks.stream()
                .map(u -> {
                    try {
                        return mapper.writeValueAsString(u);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));
        expectedJson = "[" + expectedJson + "]";
        //then
        mvc.perform(get(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testReadAllBooksShouldReturnAnEmptyArrayWhenNoBooksExist() throws Exception {
        //then
        mvc.perform(get(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testReadAllBooksShouldReturnAnEmptyArrayWhenAllExistingBooksAreDeleted() throws Exception {
        //given
        List<Book> existingBooksOnTheDatabase = Arrays.asList(
                new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, true),
                new Book(null, "book2", List.of(new Author(null, "author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, "Moscow", "8461945178395", 7, true),
                new Book(null, "book3", Arrays.asList(new Author(null, "author3"), new Author(null, "author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, "Moscow", "8461945178395", 130, true),
                new Book(null, "book4", List.of(new Author(null, "author5")), new Genre(null, "genre4"), new Publisher(null, "publisher4"), 1995, "Moscow", "8461945178395", 201, true),
                new Book(null, "book5", List.of(new Author(null, "author6")), new Genre(null, "genre5"), new Publisher(null, "publisher5"), 2012, "Moscow", "8461945178395", 12, true)
        );

        existingBooksOnTheDatabase.forEach(testEntityManager::persist);
        //then
        mvc.perform(get(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testReadBookShouldReturnTheTargetBookWhenTheBookExistsAndIsNotDeleted() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();

        //then
        mvc.perform(get(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andExpect(jsonPath("$.id", is(existingBook.getId())))
                .andExpect(jsonPath("$.title", is(existingBook.getTitle())))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].name", is(existingBook.getAuthors().get(0).getName())))
                .andExpect(jsonPath("$.genre.name", is(existingBook.getGenre().getName())))
                .andExpect(jsonPath("$.publisher.name", is(existingBook.getPublisher().getName())))
                .andExpect(jsonPath("$.publishmentYear", is(existingBook.getPublishmentYear())))
                .andExpect(jsonPath("$.deleted", is(existingBook.isDeleted())));
    }

    @Test
    public void testReadBookShouldReturnNotFoundAndEmptyBodyWhenTheBookDoesNotExist() throws Exception {
        //then
        mvc.perform(get(BASE_URL + "/" + 5464)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testReadBookShouldReturnNotFoundAndEmptyBodyWhenTheTargetBookIsDeleted() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, true));
        Integer existingBookId = existingBook.getId();
        //then
        mvc.perform(get(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

//    @Test TODO: figure out why the test does not work
//    public void testUpdateBookShouldChangeTheTargetBookWhenTheBookExists() throws Exception {
//        //given
//        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false));
//        Integer existingBookId = existingBook.getId();
//        String newTitle = "Some Book";
//        String newAuthors = "Some Human, Some Non-human";
//        String newGenre = "Interesting";
//        String newPublisher = "Smith";
//        Integer newPublishmentYear = 2014;
//        Integer newAmount = 10;
//
//        BookEditDto editDto = new BookEditDto(newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);
//
//        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
//        //then
//        mvc.perform(put(BASE_URL + "/" + existingBookId)
//                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
//                        .contentType(APPLICATION_JSON)
//                        .content(editDtoJson))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//
//        Optional<Book> updatedExistingBookOptional = bookRepository.findById(existingBookId);
//        assertThat(updatedExistingBookOptional)
//                .isNotEmpty();
//
//        Book updatedExistingBook = updatedExistingBookOptional.get();
//
//        assertThat(updatedExistingBook.getTitle())
//                .isEqualTo(newTitle);
//
//        assertThat(updatedExistingBook.getAuthors())
//                .hasSize(2);
//
//        assertThat(updatedExistingBook.getGenre().getName())
//                .isEqualTo(newGenre);
//
//        assertThat(updatedExistingBook.getPublisher().getName())
//                .isEqualTo(newPublisher);
//
//        assertThat(updatedExistingBook.getPublishmentYear())
//                .isEqualTo(newPublishmentYear);
//
//        assertThat(updatedExistingBook.getAmount())
//                .isEqualTo(newAmount);
//    }

    @Test
    public void testUpdateBookShouldReturnNotFoundWhenTheTargetBookDoesNotExist() throws Exception {
        //given
        String newTitle = "Some Book";
        List<String> newAuthors = Arrays.asList("Some Human", "Some Non-human");
        String newGenre = "Interesting";
        String newPublisher = "Smith";
        int newPublishmentYear = 2014;
        String newPublishmentLocation = "Minsk";
        String newIsbn = "9426041356172";
        int newAmount = 10;

        BookEditDto editDto = new BookEditDto(newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newPublishmentLocation, newIsbn, newAmount);
        //then
        mvc.perform(put(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookTitleIsBlank() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookTitleIsNotAWord() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "&Another One";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookAuthorsIsBlank() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = List.of("");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookAuthorsIsNotHumanNames() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("()wen $mith", "@lan &rews");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookGenreIsBlank() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookGenreIsNotAWord() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "^*cool*^";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookPublisherIsBlank() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookPublisherIsNotAWord() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "^_^";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookPublishmentYearIsEarlierThan1900() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 1867;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookPublishmentYearIsLaterThan2500() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2504;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

    @Test
    public void testUpdateBookShouldReturnBadRequestWhenTheBookAmountIsANegativeNumber() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, "Moscow", "8461945178395", 12, false));
        Integer existingBookId = existingBook.getId();
        String title = "Some Book";
        List<String> authors = Arrays.asList("Some Human", "Some Non-human");
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        String publishmentLocation = "Minsk";
        String isbn = "9426041356172";
        int amount = -4;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, publishmentLocation, isbn, amount);
        //then
        mvc.perform(put(BASE_URL + "/" + existingBookId)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", any(String.class)));
    }

//    @Test TODO: figure out why the test does not work
//    public void testDeleteBookShouldSetTheTargetBookDeletedToTrueWhenTheBookExists() throws Exception {
//        //given
//        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false));
//        Integer existingBookId = existingBook.getId();
//        //then
//        mvc.perform(delete(BASE_URL + "/" + existingBookId)
//                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(""));
//
//        Optional<Book> deletedExistingBookOptional = bookRepository.findById(existingBookId);
//        assertThat(deletedExistingBookOptional)
//                .isNotEmpty();
//
//        Book deletedExistingBook = deletedExistingBookOptional.get();
//
//        assertThat(deletedExistingBook.isDeleted())
//                .isTrue();
//    }

    @Test
    public void testDeleteBookShouldReturnNotFoundWhenTheTargetBookDoesNotExist() throws Exception {
        //then
        mvc.perform(delete(BASE_URL + "/" + 8)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
