package com.patiun.libraryspring.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patiun.libraryspring.exception.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private static String asJsonString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void testCreateBookShouldInvokeTheCreateBookMethodOfTheServiceOnce() throws Exception {
        //given
        String title = "Some Book";
        String authors = "Some Human, Some Non-human";
        String genre = "Interesting";
        String publisher = "Smith";
        int publishmentYear = 2014;
        int amount = 10;

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
    public void testReadAllBooksShouldReturnTheBookListFoundByServiceWhenTheServiceReturnsSomeBooks() throws Exception {
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

    @Test
    public void testReadAllBooksShouldReturnAnEmptyArrayWhenTheServiceReturnsNoBooks() throws Exception {
        //given
        given(service.getAllBooks())
                .willReturn(new ArrayList<>());
        //then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testReadBookShouldReturnTheBookFoundByServiceWhenTheBookExists() throws Exception {
        //given
        Integer expectedBookId = 3;
        Book expectedBook = new Book(expectedBookId, "book1", List.of(new Author(1, "author1")), new Genre(1, "genre1"), new Publisher(1, "publisher1"), 2003, 12, false);

        given(service.getBookById(expectedBookId))
                .willReturn(expectedBook);
        //then
        mvc.perform(get(BASE_URL + "/" + expectedBookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(8)))
                .andExpect(jsonPath("$.id", is(expectedBook.getId())))
                .andExpect(jsonPath("$.title", is(expectedBook.getTitle())))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].name", is(expectedBook.getAuthors().get(0).getName())))
                .andExpect(jsonPath("$.genre.name", is(expectedBook.getGenre().getName())))
                .andExpect(jsonPath("$.publisher.name", is(expectedBook.getPublisher().getName())))
                .andExpect(jsonPath("$.publishmentYear", is(expectedBook.getPublishmentYear())))
                .andExpect(jsonPath("$.deleted", is(expectedBook.isDeleted())));
    }

    @Test
    public void testReadBookShouldReturnNotFoundWhenServiceThrowsAnElementNotFoundException() throws Exception {
        //given
        Integer targetBookId = 3;

        given(service.getBookById(targetBookId))
                .willThrow(ElementNotFoundException.class);
        //then
        mvc.perform(get(BASE_URL + "/" + targetBookId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testUpdateBookShouldInvokeTheUpdateMethodOfServiceWhenTheBookExists() throws Exception {
        //given
        Integer targetBookId = 14;
        String newTitle = "Peace and War";
        String newAuthors = "Tolstoy Leo";
        String newGenre = "Inverted Novel";
        String newPublisher = "Some dude";
        int newPublishmentYear = 2020;
        int newAmount = 100;

        BookEditDto editDto = new BookEditDto(newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);

        String updateDtoJson = new ObjectMapper().writeValueAsString(editDto);
        //then
        mvc.perform(put(BASE_URL + "/" + targetBookId)
                        .contentType(APPLICATION_JSON)
                        .content(updateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .updateBookById(targetBookId, newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);
    }

    @Test
    public void testUpdateBookShouldReturnNotFoundWhenServiceThrowsAnElementNotFoundException() throws Exception {
        //given
        Integer targetBookId = 14;
        String newTitle = "Peace and War";
        String newAuthors = "Tolstoy Leo";
        String newGenre = "Inverted Novel";
        String newPublisher = "Some dude";
        int newPublishmentYear = 2020;
        int newAmount = 100;

        BookEditDto editDto = new BookEditDto(newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);

        String updateDtoJson = new ObjectMapper().writeValueAsString(editDto);

        doThrow(ElementNotFoundException.class)
                .when(service)
                .updateBookById(targetBookId, newTitle, newAuthors, newGenre, newPublisher, newPublishmentYear, newAmount);
        //then
        mvc.perform(put(BASE_URL + "/" + targetBookId)
                        .contentType(APPLICATION_JSON)
                        .content(updateDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testDeleteBookShouldInvokeTheMethodOfServiceWhenTheBookExists() throws Exception {
        //given
        Integer targetBookId = 14;
        //then
        mvc.perform(delete(BASE_URL + "/" + targetBookId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(service)
                .should(times(1))
                .deleteBookById(targetBookId);
    }

    @Test
    public void testDeleteBookShouldReturnNotFoundWhenServiceThrowsAnElementNotFoundException() throws Exception {
        //given
        Integer targetBookId = 14;

        doThrow(ElementNotFoundException.class)
                .when(service)
                .deleteBookById(targetBookId);
        //then
        mvc.perform(delete(BASE_URL + "/" + targetBookId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
