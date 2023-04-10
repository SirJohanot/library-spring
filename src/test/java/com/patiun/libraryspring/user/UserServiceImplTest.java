package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ElementNotFoundException;
import com.patiun.libraryspring.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void loadUserByUsernameShouldReturnUserFoundByRepositoryWhenUserIsPresent() {
        //given
        String username = "user";

        User userFoundByRepository = new User(1, username, "123", "john", "smith", false, UserRole.READER);

        given(userRepository.findByLogin(username))
                .willReturn(Optional.of(userFoundByRepository));
        //when
        UserDetails actualUser = userService.loadUserByUsername(username);
        //then
        assertThat(actualUser)
                .isEqualTo(userFoundByRepository);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenRepositoryCouldNotFindTheUser() {
        //given
        String username = "user";

        given(userRepository.findByLogin(username))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void signUpShouldSaveUserWithRepositoryWhenUserHasUniqueLogin() throws ServiceException {
        //given
        String login = "user";
        String password = "12345678";
        String firstName = "john";
        String lastName = "doe";

        given(userRepository.findByLogin(login))
                .willReturn(Optional.empty());

        String encodedPassword = "hk2k5h4g655436";
        given(passwordEncoder.encode(password))
                .willReturn(encodedPassword);

        User expectedUserToBeSaved = new User(null, login, encodedPassword, firstName, lastName, false, UserRole.READER);
        //when
        userService.signUp(login, password, firstName, lastName);
        //then
        then(userRepository)
                .should(times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void signUpShouldThrowServiceExceptionWhenUserHasExistingLogin() {
        //given
        String login = "user";
        String password = "12345678";
        String encodedPassword = "hk2k5h4g655436";
        String firstName = "john";
        String lastName = "doe";

        User expectedUserToBeFound = new User(null, login, encodedPassword, firstName, lastName, false, UserRole.READER);
        given(userRepository.findByLogin(login))
                .willReturn(Optional.of(expectedUserToBeFound));
        //then
        assertThatThrownBy(() -> userService.signUp(login, password, firstName, lastName))
                .isInstanceOf(ServiceException.class);
        then(userRepository)
                .should(never())
                .save(any());
    }

    @Test
    public void getAllUsersShouldReturnUsersFoundByRepository() {
        //given
        List<User> usersFoundByRepository = Arrays.asList(
                new User(1, "coolGuy", "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN),
                new User(2, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER)
        );
        given(userRepository.findAll())
                .willReturn(usersFoundByRepository);
        //when
        List<User> actualUsersReturnedByService = userService.getAllUsers();
        //then
        assertThat(actualUsersReturnedByService)
                .isEqualTo(usersFoundByRepository);
    }

    @Test
    public void getAllAdminsShouldReturnAdminsFoundByRepository() {
        //given
        List<User> adminsFoundByRepository = Arrays.asList(
                new User(1, "coolGuy", "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN),
                new User(2, "ookla", "h46jh53h6", "john", "doe", true, UserRole.ADMIN),
                new User(3, "gfjghj", "ghj", "john", "s", false, UserRole.ADMIN)
        );
        given(userRepository.findByRoleIs(UserRole.ADMIN))
                .willReturn(adminsFoundByRepository);
        //when
        List<User> actualAdminsReturnedByService = userService.getAllAdmins();
        //then
        assertThat(actualAdminsReturnedByService)
                .isEqualTo(adminsFoundByRepository);
    }

    @Test
    public void getUserByIdShouldReturnUserFoundByRepositoryWhenUserExists() {
        //given
        Integer targetUserId = 2;

        User userFoundByRepository = new User(targetUserId, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER);
        given(userRepository.findById(targetUserId))
                .willReturn(Optional.of(userFoundByRepository));
        //when
        User actualUserReturnedByService = userService.getUserById(targetUserId);
        //then
        assertThat(actualUserReturnedByService)
                .isEqualTo(userFoundByRepository);
    }

    @Test
    public void getUserByIdShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 2;
        given(userRepository.findById(targetUserId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.getUserById(targetUserId))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void getUserByLoginShouldReturnUserFoundByRepositoryWhenUserExists() {
        //given
        String targetUserLogin = "johnDoe";

        User userFoundByRepository = new User(2, targetUserLogin, "h46jh53h6", "john", "doe", true, UserRole.READER);
        given(userRepository.findByLogin(targetUserLogin))
                .willReturn(Optional.of(userFoundByRepository));
        //when
        User actualUserReturnedByService = userService.getUserByLogin(targetUserLogin);
        //then
        assertThat(actualUserReturnedByService)
                .isEqualTo(userFoundByRepository);
    }

    @Test
    public void getUserByLoginShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        String targetUserLogin = "johnDoe";
        given(userRepository.findByLogin(targetUserLogin))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.getUserByLogin(targetUserLogin))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void updateUserByIdShouldSaveUserWithRepositoryWhenUserExists() {
        //given
        Integer targetUserId = 436;
        String newFirstName = "jack";
        String newLastName = "stevens";
        UserRole newRole = UserRole.LIBRARIAN;

        String oldLogin = "login";
        String oldPassword = "d8f6g56sdf";
        String oldFirstName = "john";
        String oldLastName = "doe";
        boolean oldBlocked = false;
        UserRole oldRole = UserRole.READER;

        User userReturnedByRepository = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, oldBlocked, oldRole);
        given(userRepository.findById(targetUserId))
                .willReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, newFirstName, newLastName, oldBlocked, newRole);
        //when
        userService.updateUserById(targetUserId, newFirstName, newLastName, newRole);
        //then
        then(userRepository)
                .should(times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void updateUserByIdShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 436;
        String newFirstName = "jack";
        String newLastName = "stevens";
        UserRole newRole = UserRole.LIBRARIAN;

        given(userRepository.findById(targetUserId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.updateUserById(targetUserId, newFirstName, newLastName, newRole))
                .isInstanceOf(ElementNotFoundException.class);
        then(userRepository)
                .should(never())
                .save(any());
    }

    @Test
    public void switchUserBlockedShouldSaveBlockedUserWhenUserIsUnblocked() {
        //given
        Integer targetUserId = 436;

        String oldLogin = "login";
        String oldPassword = "d8f6g56sdf";
        String oldFirstName = "john";
        String oldLastName = "doe";
        UserRole oldRole = UserRole.READER;

        User userReturnedByRepository = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, false, oldRole);
        given(userRepository.findById(targetUserId))
                .willReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, true, oldRole);
        //when
        userService.switchUserBlockedById(targetUserId);
        //then
        then(userRepository)
                .should(times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void switchUserBlockedShouldSaveUnblockedUserWhenUserIsBlocked() {
        //given
        Integer targetUserId = 436;

        String oldLogin = "login";
        String oldPassword = "d8f6g56sdf";
        String oldFirstName = "john";
        String oldLastName = "doe";
        UserRole oldRole = UserRole.READER;

        User userReturnedByRepository = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, true, oldRole);
        given(userRepository.findById(targetUserId))
                .willReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, false, oldRole);
        //when
        userService.switchUserBlockedById(targetUserId);
        //then
        then(userRepository)
                .should(times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void switchUserBlockedShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 436;

        given(userRepository.findById(targetUserId))
                .willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.switchUserBlockedById(targetUserId))
                .isInstanceOf(ElementNotFoundException.class);
        then(userRepository)
                .should(never())
                .save(any());
    }
}
