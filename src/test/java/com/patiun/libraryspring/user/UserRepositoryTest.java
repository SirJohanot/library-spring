package com.patiun.libraryspring.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void findByLoginShouldReturnOptionalOfTheUserWithTheLoginWhenUserExists() {
        //given
        User firstUser = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(firstUser);

        User secondUser = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(secondUser);

        String targetUserLogin = "targetLogin";
        User thirdUser = new User(null, targetUserLogin, "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(thirdUser);

        entityManager.flush();
        //when
        Optional<User> actualResult = userRepository.findByLogin(targetUserLogin);
        //then
        assertThat(actualResult)
                .hasValue(thirdUser);
    }

    @Test
    public void findByLoginShouldReturnEmptyOptionalWhenUserDoesNotExist() {
        //given
        User firstUser = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(firstUser);

        User secondUser = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(secondUser);

        User thirdUser = new User(null, "someOtherLogin", "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(thirdUser);

        entityManager.flush();
        //When
        Optional<User> actualResult = userRepository.findByLogin("someLogin");
        //then
        assertThat(actualResult)
                .isEmpty();
    }

    @Test
    public void findByRoleIsShouldReturnListOfTheUsersWithRolesWithTheLoginWhenUsersExist() {
        //given
        User firstUser = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(firstUser);

        User secondUser = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.ADMIN);
        entityManager.persist(secondUser);

        User thirdUser = new User(null, "targetLogin", "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(thirdUser);

        entityManager.flush();
        //when
        List<User> actualResult = userRepository.findByRoleIs(UserRole.READER);
        //then
        assertThat(actualResult)
                .hasSize(2)
                .contains(firstUser, thirdUser);
    }

    @Test
    public void findByRoleIsShouldReturnEmptyListWhenUsersDoNotExist() {
        //given
        User firstUser = new User(null, "userLoginOne", "hkjhgkjhgkhfj", "john", "smith", false, UserRole.READER);
        entityManager.persist(firstUser);

        User secondUser = new User(null, "userLoginTwo", "12jgkhjgkjhgk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(secondUser);

        User thirdUser = new User(null, "someOtherLogin", "12gjkjhkgjhk3", "john", "smith", false, UserRole.READER);
        entityManager.persist(thirdUser);

        entityManager.flush();
        //When
        List<User> actualResult = userRepository.findByRoleIs(UserRole.LIBRARIAN);
        //then
        assertThat(actualResult)
                .isEmpty();
    }
}