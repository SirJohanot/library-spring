package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void testFindByIdShouldReturnOptionalOfTheBookWithTheIdWhenBookExists() {
        //given
        entityManager.persist(new Book(null, "book1", List.of(new Author("author1")), List.of(new Editor(null, "editorRole1", "editorName1")), new Genre(null, "genre1"), new Publisher(null, "publisher1", "publisherPostalCode1", "publihserAddress1"), new PrintingHouse(null, "printingHouse1", "printingHousePostalCode1", "printingHouseAddress1"), 2003, "Minsk", "description1", 20, "3298614390153", "80.7", "33.4", 12, false));

        entityManager.persist(new Book(null, "book2", List.of(new Author("author2")), List.of(new Editor(null, "editorRole2", "editorName2")), new Genre(null, "genre2"), new Publisher(null, "publisher2", "publisherPostalCode2", "publihserAddress2"), new PrintingHouse(null, "printingHouse2", "printingHousePostalCode2", "printingHouseAddress2"), 1998, "Minsk", "description2", 230, "3298614230153", "81.7", "23.4", 9, false));

        Book thirdBook = new Book(null, "book3", List.of(new Author("author3")), List.of(new Editor(null, "editorRole3", "editorName3")), new Genre(null, "genre3"), new Publisher(null, "publisher3", "publisherPostalCode3", "publihserAddress3"), new PrintingHouse(null, "printingHouse3", "printingHousePostalCode3", "printingHouseAddress3"), 1995, "Minsk", "description3", 60, "3123614390153", "70.7", "33.6", 8, false);
        Integer targetBookId = entityManager.persistAndGetId(thirdBook, Integer.class);

        entityManager.flush();
        //when
        Optional<Book> actualResult = bookRepository.findById(targetBookId);
        //then
        assertThat(actualResult)
                .hasValue(thirdBook);
    }

    @Test
    public void testFindByIdShouldReturnEmptyOptionalWhenBookDoesNotExist() {
        //given
        Integer firstBookId = entityManager.persistAndGetId(new Book(null, "book1", List.of(new Author("author1")), List.of(new Editor(null, "editorRole1", "editorName1")), new Genre(null, "genre1"), new Publisher(null, "publisher1", "publisherPostalCode1", "publihserAddress1"), new PrintingHouse(null, "printingHouse1", "printingHousePostalCode1", "printingHouseAddress1"), 2003, "Minsk", "description1", 20, "3298614390153", "80.7", "33.4", 12, false), Integer.class);

        Integer secondBookId = entityManager.persistAndGetId(new Book(null, "book2", List.of(new Author("author2")), List.of(new Editor(null, "editorRole2", "editorName2")), new Genre(null, "genre2"), new Publisher(null, "publisher2", "publisherPostalCode2", "publihserAddress2"), new PrintingHouse(null, "printingHouse2", "printingHousePostalCode2", "printingHouseAddress2"), 1998, "Minsk", "description2", 230, "3298614230153", "81.7", "23.4", 9, false), Integer.class);

        Integer thirdBookId = entityManager.persistAndGetId(new Book(null, "book3", List.of(new Author("author3")), List.of(new Editor(null, "editorRole3", "editorName3")), new Genre(null, "genre3"), new Publisher(null, "publisher3", "publisherPostalCode3", "publihserAddress3"), new PrintingHouse(null, "printingHouse3", "printingHousePostalCode3", "printingHouseAddress3"), 1995, "Minsk", "description3", 60, "3123614390153", "70.7", "33.6", 8, false), Integer.class);

        entityManager.flush();

        Integer targetId = 1;
        while (targetId.equals(firstBookId) || targetId.equals(secondBookId) || targetId.equals(thirdBookId)) {
            targetId++;
        }
        //when
        Optional<Book> actualResult = bookRepository.findById(targetId);
        //then
        assertThat(actualResult)
                .isEmpty();
    }

    @Test
    public void testFindAllByDeletedFalseShouldReturnAllNonDeletedBooksWhenSuchBooksExist() {
        //given
        Book firstBook = new Book(null, "book1", List.of(new Author("author1")), List.of(new Editor(null, "editorRole1", "editorName1")), new Genre(null, "genre1"), new Publisher(null, "publisher1", "publisherPostalCode1", "publihserAddress1"), new PrintingHouse(null, "printingHouse1", "printingHousePostalCode1", "printingHouseAddress1"), 2003, "Minsk", "description1", 20, "3298614390153", "80.7", "33.4", 12, false);
        entityManager.persist(firstBook);

        entityManager.persist(new Book(null, "book2", List.of(new Author("author2")), List.of(new Editor(null, "editorRole2", "editorName2")), new Genre(null, "genre2"), new Publisher(null, "publisher2", "publisherPostalCode2", "publihserAddress2"), new PrintingHouse(null, "printingHouse2", "printingHousePostalCode2", "printingHouseAddress2"), 1998, "Minsk", "description2", 230, "3298614230153", "81.7", "23.4", 9, true));

        Book thirdBook = new Book(null, "book3", List.of(new Author("author3")), List.of(new Editor(null, "editorRole3", "editorName3")), new Genre(null, "genre3"), new Publisher(null, "publisher3", "publisherPostalCode3", "publihserAddress3"), new PrintingHouse(null, "printingHouse3", "printingHousePostalCode3", "printingHouseAddress3"), 1995, "Minsk", "description3", 60, "3123614390153", "70.7", "33.6", 8, false);
        entityManager.persist(thirdBook);

        entityManager.flush();
        //when
        List<Book> actualResult = bookRepository.findAllByIsDeletedFalse();
        //then
        assertThat(actualResult)
                .hasSize(2)
                .contains(firstBook, thirdBook);
    }

    @Test
    public void testFindAllByDeletedFalseShouldReturnAnEmptyListWhenSuchBooksDoNotExist() {
        //given
        entityManager.persist(new Book(null, "book1", List.of(new Author("author1")), List.of(new Editor(null, "editorRole1", "editorName1")), new Genre(null, "genre1"), new Publisher(null, "publisher1", "publisherPostalCode1", "publihserAddress1"), new PrintingHouse(null, "printingHouse1", "printingHousePostalCode1", "printingHouseAddress1"), 2003, "Minsk", "description1", 20, "3298614390153", "80.7", "33.4", 12, true));

        entityManager.persist(new Book(null, "book2", List.of(new Author("author2")), List.of(new Editor(null, "editorRole2", "editorName2")), new Genre(null, "genre2"), new Publisher(null, "publisher2", "publisherPostalCode2", "publihserAddress2"), new PrintingHouse(null, "printingHouse2", "printingHousePostalCode2", "printingHouseAddress2"), 1998, "Minsk", "description2", 230, "3298614230153", "81.7", "23.4", 9, true));

        entityManager.persist(new Book(null, "book3", List.of(new Author("author3")), List.of(new Editor(null, "editorRole3", "editorName3")), new Genre(null, "genre3"), new Publisher(null, "publisher3", "publisherPostalCode3", "publihserAddress3"), new PrintingHouse(null, "printingHouse3", "printingHousePostalCode3", "printingHouseAddress3"), 1995, "Minsk", "description3", 60, "3123614390153", "70.7", "33.6", 8, true));

        entityManager.flush();
        //when
        List<Book> actualResult = bookRepository.findAllByIsDeletedFalse();
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}
