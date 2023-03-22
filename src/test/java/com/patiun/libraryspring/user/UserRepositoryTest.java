package com.patiun.libraryspring.user;

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
public class UserRepositoryTest {

    private final TestEntityManager entityManager;

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(TestEntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Test
    public void findByLoginShouldReturnOptionalOfTheUserWithTheLoginWhenUserExists(){
        //given
        User user1 = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(user1);

        User user2 = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(user2);

        String targetUserLogin = "targetLogin";
        User user3 = new User(null, targetUserLogin, "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(user3);

        entityManager.flush();

        Optional<User> expectedResult = Optional.of(user3);
        //when
        Optional<User> actualResult = userRepository.findByLogin(targetUserLogin);
        //then
        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void findByLoginShouldReturnEmptyOptionalWhenUserDoesNotExist(){
        //given
        User user1 = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(user1);

        User user2 = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(user2);

        User user3 = new User(null, "someOtherLogin", "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(user3);

        entityManager.flush();
        //When
        Optional<User> actualResult = userRepository.findByLogin("someLogin");
        //then
        assertThat(actualResult)
                .isEqualTo(Optional.empty());
    }
}