package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    public void createBookShouldSaveNewBookWhenPrimarySubjectsDoNotExist() {
        //given
        String title = "War and Peace";
        String authors = "Leo Tolstoy";
        String genreName = "Historical Novel";
        String publisherName = "Hardcover";
        Integer publishmentYear = 2014;
        Integer amount = 16;

        given(authorRepository.findOptionalByName(authors))
                .willReturn(Optional.empty());
        given(genreRepository.findOptionalByName(genreName))
                .willReturn(Optional.empty());
        given(publisherRepository.findOptionalByName(publisherName))
                .willReturn(Optional.empty());
        //when
        bookService.createBook(title, authors, genreName, publisherName, publishmentYear, amount);
        //then
        then(bookRepository)
                .should(times(1))
                .save(new Book(null, title, List.of(new Author(authors)), new Genre(null, genreName), new Publisher(null, publisherName), publishmentYear, amount, false));
    }
}
