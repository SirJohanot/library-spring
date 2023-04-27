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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testReadAllBooksShouldReturnTheBookListOfUndeletedBooksWhenNoBooksAreDeleted() throws Exception {
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

}
