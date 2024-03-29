package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PublisherRepositoryTest {

    private final TestEntityManager entityManager;

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherRepositoryTest(TestEntityManager entityManager, PublisherRepository publisherRepository) {
        this.entityManager = entityManager;
        this.publisherRepository = publisherRepository;
    }

    @Test
    public void testFindByNameShouldReturnOptionalOfThePublisherWithTheNameWhenPublisherExists() {
        //given
        entityManager.persist(new Publisher(null, "Hardcover"));

        entityManager.persist(new Publisher(null, "Book Worm"));

        String thirdPublisherName = "Target Publisher";
        Publisher thirdPublisher = new Publisher(null, thirdPublisherName);
        entityManager.persist(thirdPublisher);

        entityManager.flush();
        //when
        Optional<Publisher> actualResult = publisherRepository.findByName(thirdPublisherName);
        //then
        assertThat(actualResult)
                .hasValue(thirdPublisher);
    }

    @Test
    public void testFindByNameShouldReturnEmptyOptionalWhenPublisherDoesNotExist() {
        //given
        entityManager.persist(new Publisher(null, "Hardcover"));

        entityManager.persist(new Publisher(null, "Book Worm"));

        entityManager.persist(new Publisher(null, "Target Publisher"));

        entityManager.flush();
        //when
        Optional<Publisher> actualResult = publisherRepository.findByName("Blah blah blah");
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}
