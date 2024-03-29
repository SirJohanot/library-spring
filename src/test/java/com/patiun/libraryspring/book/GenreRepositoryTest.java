package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GenreRepositoryTest {

    private final TestEntityManager entityManager;

    private final GenreRepository genreRepository;

    @Autowired
    public GenreRepositoryTest(TestEntityManager entityManager, GenreRepository genreRepository) {
        this.entityManager = entityManager;
        this.genreRepository = genreRepository;
    }

    @Test
    public void testFindByNameShouldReturnOptionalOfTheGenreWithTheNameWhenGenreExists() {
        //given
        String firstGenreName = "Horror";
        Genre firstGenre = new Genre(null, firstGenreName);
        entityManager.persist(firstGenre);

        entityManager.persist(new Genre(null, "Fantasy"));

        entityManager.persist(new Genre(null, "Detective"));

        entityManager.flush();
        //when
        Optional<Genre> actualResult = genreRepository.findByName(firstGenreName);
        //then
        assertThat(actualResult)
                .hasValue(firstGenre);
    }

    @Test
    public void testFindByNameShouldReturnEmptyOptionalWhenGenreDoesNotExist() {
        //given
        entityManager.persist(new Genre(null, "Horror"));

        entityManager.persist(new Genre(null, "Fantasy"));

        entityManager.persist(new Genre(null, "Detective"));

        entityManager.flush();
        //when
        Optional<Genre> actualResult = genreRepository.findByName("Historical novel");
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}
