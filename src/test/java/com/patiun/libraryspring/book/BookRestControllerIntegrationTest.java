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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        String authors = "Some Human, Some Non-human";
        String genre = "Interesting";
        String publisher = "Smith";
        Integer publishmentYear = 2014;
        Integer amount = 10;

        BookEditDto editDto = new BookEditDto(title, authors, genre, publisher, publishmentYear, amount);

        String editDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(post(BASE_URL)
                        .with(httpBasic(DUMMY_ADMIN_CREDENTIALS, DUMMY_ADMIN_CREDENTIALS))
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertThat(bookRepository.findAll())
                .hasSize(1)
                .allMatch(b -> Objects.equals(b.getTitle(), title) &&
                        Objects.equals(b.getAuthors().stream()
                                .map(Author::getName)
                                .collect(Collectors.joining(", ")), authors) &&
                        Objects.equals(b.getGenre().getName(), genre) &&
                        Objects.equals(b.getPublisher().getName(), publisher) &&
                        Objects.equals(b.getPublishmentYear(), publishmentYear) &&
                        Objects.equals(b.getAmount(), amount)
                );
    }

    @Test
    public void testReadAllBooksShouldReturnTheBookListOfUndeletedBooksWhenNoExistingBooksAreDeleted() throws Exception {
        //given
        List<Book> existingBooksOnTheDatabase = Arrays.asList(
                new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false),
                new Book(null, "book2", List.of(new Author(null, "author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, false),
                new Book(null, "book3", Arrays.asList(new Author(null, "author3"), new Author(null, "author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, 130, false)
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
                new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, true),
                new Book(null, "book2", List.of(new Author(null, "author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, true),
                new Book(null, "book3", Arrays.asList(new Author(null, "author3"), new Author(null, "author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, 130, false),
                new Book(null, "book4", List.of(new Author(null, "author5")), new Genre(null, "genre4"), new Publisher(null, "publisher4"), 1995, 201, true),
                new Book(null, "book5", List.of(new Author(null, "author6")), new Genre(null, "genre5"), new Publisher(null, "publisher5"), 2012, 12, false)
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
    public void testReadBookShouldReturnTheTargetBookWhenTheBookExists() throws Exception {
        //given
        Book existingBook = testEntityManager.persist(new Book(null, "book1", List.of(new Author(null, "author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false));
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

}