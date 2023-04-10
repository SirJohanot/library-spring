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
public class AuthorRepositoryTest {

    private final TestEntityManager entityManager;

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorRepositoryTest(TestEntityManager entityManager, AuthorRepository authorRepository) {
        this.entityManager = entityManager;
        this.authorRepository = authorRepository;
    }

    @Test
    public void findByNameShouldReturnOptionalOfTheAuthorWithTheNameWhenAuthorExists() {
        //given
        entityManager.persist(new Author("George Orwell"));

        String secondAuthorName = "Kanstantsin Mikhailovich Mitskievich";
        Author secondAuthor = new Author(secondAuthorName);
        entityManager.persist(secondAuthor);

        entityManager.persist(new Author("Mikhail Lermontov"));

        entityManager.flush();
        //when
        Optional<Author> actualResult = authorRepository.findByName(secondAuthorName);
        //then
        assertThat(actualResult)
                .hasValue(secondAuthor);
    }

    @Test
    public void findByNameShouldReturnEmptyOptionalWhenAuthorDoesNotExist() {
        //given
        entityManager.persist(new Author("George Orwell"));

        entityManager.persist(new Author("Kanstantsin Mikhailovich Mitskievich"));

        entityManager.persist(new Author("Mikhail Lermontov"));

        entityManager.flush();
        //when
        Optional<Author> actualResult = authorRepository.findByName("Maksim Tank");
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}
