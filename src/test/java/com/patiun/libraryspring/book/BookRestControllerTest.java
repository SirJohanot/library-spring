package com.patiun.libraryspring.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookRestControllerTest {

    private static final String BASE_URL = "/books";

    @MockBean
    private BookService service;

    private final MockMvc mvc;

    @Autowired
    public BookRestControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void testCreateBookShouldInvokeTheCreateBookMethodOfTheServiceOnce() throws Exception {
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
                        .contentType(APPLICATION_JSON)
                        .content(editDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .createBook(title, authors, genre, publisher, publishmentYear, amount);
    }

    @Test
    public void testReadAllBooksShouldReturnTheBookListFoundByService() throws Exception {
        //given
        List<Book> booksFoundByService = Arrays.asList(
                new Book(1, "book1", List.of(new Author(1, "author1")), new Genre(1, "genre1"), new Publisher(1, "publisher1"), 2003, 12, false),
                new Book(2, "book2", List.of(new Author(1, "author1")), new Genre(2, "genre2"), new Publisher(2, "publisher2"), 1998, 7, false),
                new Book(3, "book3", Arrays.asList(new Author(1, "author1"), new Author(2, "author2")), new Genre(1, "genre1"), new Publisher(3, "publisher3"), 2014, 130, false)
        );

        given(service.getAllBooks())
                .willReturn(booksFoundByService);

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = booksFoundByService.stream()
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
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

}
