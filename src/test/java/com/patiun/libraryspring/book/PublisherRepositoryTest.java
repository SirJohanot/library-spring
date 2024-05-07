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
    public void testFindByNameAndPostalCodeAndAddressShouldReturnOptionalOfThePublisherWithTheNameWhenPublisherExists() {
        //given
        entityManager.persist(new Publisher(null, "Hardcover", "347568", "John City"));

        entityManager.persist(new Publisher(null, "Book Worm", "547476", "John City"));

        String thirdPublisherName = "Target Publisher";
        String thirdPublisherPostalCode = "834751";
        String thirdPublisherAddress = "PubCity";
        Publisher thirdPublisher = new Publisher(null, thirdPublisherName, thirdPublisherPostalCode, thirdPublisherAddress);
        entityManager.persist(thirdPublisher);

        entityManager.flush();
        //when
        Optional<Publisher> actualResult = publisherRepository.findByNameAndPostalCodeAndAddress(thirdPublisherName, thirdPublisherPostalCode, thirdPublisherAddress);
        //then
        assertThat(actualResult)
                .hasValue(thirdPublisher);
    }

    @Test
    public void testFindByNameAndPostalCodeAndAddressShouldReturnEmptyOptionalWhenPublisherDoesNotExist() {
        //given
        entityManager.persist(new Publisher(null, "Hardcover", "347568", "John City"));

        entityManager.persist(new Publisher(null, "Book Worm", "347568", "John City"));

        entityManager.persist(new Publisher(null, "Target Publisher", "347568", "John City"));

        entityManager.flush();
        //when
        Optional<Publisher> actualResult = publisherRepository.findByNameAndPostalCodeAndAddress("Blah blah blah", "347568", "John City");
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}
