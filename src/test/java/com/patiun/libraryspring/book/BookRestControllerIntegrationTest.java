package com.patiun.libraryspring.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class BookRestControllerIntegrationTest {

    private static final String BASE_URL = "/books";

    private static final String DUMMY_ADMIN_CREDENTIALS = "admin";

    private final MockMvc mvc;

    private final BookRepository bookRepository;

    @Autowired
    public BookRestControllerIntegrationTest(MockMvc mvc, BookRepository bookRepository) {
        this.mvc = mvc;
        this.bookRepository = bookRepository;
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

}
