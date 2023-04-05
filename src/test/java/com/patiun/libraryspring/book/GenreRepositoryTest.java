package com.patiun.libraryspring.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
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
    public void findByNameShouldReturnOptionalOfTheGenreWithTheNameWhenGenreExists() {
        //given
        String firstGenreName = "Horror";
        Genre firstGenre = new Genre(null, firstGenreName);
        entityManager.persist(firstGenre);

        entityManager.persist(new Genre(null, "Fantasy"));

        entityManager.persist(new Genre(null, "Detective"));

        entityManager.flush();

        Optional<Genre> expectedResult = Optional.of(firstGenre);
        //when
        Optional<Genre> actualResult = genreRepository.findByName(firstGenreName);
        //then
        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void findByNameShouldReturnEmptyOptionalWhenGenreDoesNotExist() {
        //given
        entityManager.persist(new Genre(null, "Horror"));

        entityManager.persist(new Genre(null, "Fantasy"));

        entityManager.persist(new Genre(null, "Detective"));

        entityManager.flush();
        //when
        Optional<Genre> actualResult = genreRepository.findByName("Historical novel");
        //then
        assertThat(actualResult)
                .isEqualTo(Optional.empty());
    }
}
