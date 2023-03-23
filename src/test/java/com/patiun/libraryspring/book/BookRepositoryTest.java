package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

    private final TestEntityManager entityManager;

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(TestEntityManager entityManager, BookRepository bookRepository) {
        this.entityManager = entityManager;
        this.bookRepository = bookRepository;
    }

    @Test
    public void findByIdShouldReturnOptionalOfTheBookWithTheIdWhenBookExists() {
        //given
        entityManager.persist(new Book(null, "book1", List.of(new Author("author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false));

        entityManager.persist(new Book(null, "book2", List.of(new Author("author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, false));

        Book thirdBook = new Book(null, "book3", Arrays.asList(new Author("author3"), new Author("author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, 130, false);
        Integer targetBookId = entityManager.persistAndGetId(thirdBook, Integer.class);

        entityManager.flush();

        Optional<Book> expectedResult = Optional.of(thirdBook);
        //when
        Optional<Book> actualResult = bookRepository.findById(targetBookId);
        //then
        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenBookDoesNotExist() {
        //given
        Integer firstBookId = entityManager.persistAndGetId(new Book(null, "book1", List.of(new Author("author1")), new Genre(null, "genre1"), new Publisher(null, "publisher1"), 2003, 12, false), Integer.class);

        Integer secondBookId = entityManager.persistAndGetId(new Book(null, "book2", List.of(new Author("author2")), new Genre(null, "genre2"), new Publisher(null, "publisher2"), 1998, 7, false), Integer.class);

        Integer thirdBookId = entityManager.persistAndGetId(new Book(null, "book3", Arrays.asList(new Author("author3"), new Author("author4")), new Genre(null, "genre3"), new Publisher(null, "publisher3"), 2014, 130, false), Integer.class);

        entityManager.flush();

        Integer targetId = 1;
        while (targetId.equals(firstBookId) || targetId.equals(secondBookId) || targetId.equals(thirdBookId)) {
            targetId++;
        }
        //when
        Optional<Book> actualResult = bookRepository.findById(targetId);
        //then
        assertThat(actualResult)
                .isEqualTo(Optional.empty());
    }
}
