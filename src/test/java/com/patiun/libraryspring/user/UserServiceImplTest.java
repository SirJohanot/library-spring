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
    public void testLoadUserByUsernameShouldReturnUserFoundByRepositoryWhenUserIsPresent() {
        //given
        String username = "user";
        User expectedUser = new User(1, username, "123", "john", "smith", false, UserRole.READER);
        when(userRepository.findByLogin(username))
                .thenReturn(Optional.of(expectedUser));
        //when
        UserDetails actualUser = userService.loadUserByUsername(username);
        //then
        assertThat(actualUser)
                .isEqualTo(expectedUser);
    }

    @Test
    public void testLoadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenRepositoryCouldNotFindTheUser() {
        //given
        String username = "user";
        when(userRepository.findByLogin(username))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void testSignUpShouldSaveUserWithRepositoryWhenUserHasUniqueLogin() throws ServiceException {
        //given
        String login = "user";
        String password = "12345678";
        String firstName = "john";
        String lastName = "doe";

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.empty());

        String encodedPassword = "hk2k5h4g655436";
        when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);

        User expectedUserToBeSaved = new User(null, login, encodedPassword, firstName, lastName, false, UserRole.READER);
        //when
        userService.signUp(login, password, firstName, lastName);
        //then
        verify(userRepository, times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void testSignUpShouldThrowServiceExceptionWhenUserHasExistingLogin() {
        //given
        String login = "user";
        String password = "12345678";
        String encodedPassword = "hk2k5h4g655436";
        String firstName = "john";
        String lastName = "doe";

        User expectedUserToBeFound = new User(null, login, encodedPassword, firstName, lastName, false, UserRole.READER);
        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(expectedUserToBeFound));

        //then
        assertThatThrownBy(() -> userService.signUp(login, password, firstName, lastName))
                .isInstanceOf(ServiceException.class);
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void testGetAllUsersShouldReturnUsersFoundByRepository() {
        //given
        List<User> usersReturnedByRepository = Arrays.asList(
                new User(1, "coolGuy", "86gfd5df", "jack", "buckwheat", false, UserRole.ADMIN),
                new User(2, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER)
        );
        when(userRepository.findAll())
                .thenReturn(usersReturnedByRepository);
        //when
        List<User> actualUsersReturnedByService = userService.getAllUsers();
        //then
        assertThat(actualUsersReturnedByService)
                .isEqualTo(usersReturnedByRepository);
    }

    @Test
    public void testGetUserByIdShouldReturnUserFoundByRepositoryWhenUserExists() {
        //given
        Integer targetUserId = 2;
        User userReturnedByRepository = new User(targetUserId, "ookla", "h46jh53h6", "john", "doe", true, UserRole.READER);
        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.of(userReturnedByRepository));
        //when
        User actualUserReturnedByService = userService.getUserById(targetUserId);
        //then
        assertThat(actualUserReturnedByService)
                .isEqualTo(userReturnedByRepository);
    }

    @Test
    public void testGetUserByIdShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 2;
        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.getUserById(targetUserId))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void testGetUserByLoginShouldReturnUserFoundByRepositoryWhenUserExists() {
        //given
        String targetUserLogin = "johnDoe";
        User userReturnedByRepository = new User(2, targetUserLogin, "h46jh53h6", "john", "doe", true, UserRole.READER);
        when(userRepository.findByLogin(targetUserLogin))
                .thenReturn(Optional.of(userReturnedByRepository));
        //when
        User actualUserReturnedByService = userService.getUserByLogin(targetUserLogin);
        //then
        assertThat(actualUserReturnedByService)
                .isEqualTo(userReturnedByRepository);
    }

    @Test
    public void testGetUserByLoginShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        String targetUserLogin = "johnDoe";
        when(userRepository.findByLogin(targetUserLogin))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.getUserByLogin(targetUserLogin))
                .isInstanceOf(ElementNotFoundException.class);
    }

    @Test
    public void testUpdateUserByIdShouldSaveUserWithRepositoryWhenUserExists() {
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
        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, newFirstName, newLastName, oldBlocked, newRole);
        //when
        userService.updateUserById(targetUserId, newFirstName, newLastName, newRole);
        //then
        verify(userRepository, times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void testUpdateUserByIdShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 436;
        String newFirstName = "jack";
        String newLastName = "stevens";
        UserRole newRole = UserRole.LIBRARIAN;

        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.updateUserById(targetUserId, newFirstName, newLastName, newRole))
                .isInstanceOf(ElementNotFoundException.class);
        verify(userRepository, never())
                .save(any());
    }

    @Test
    public void testSwitchUserBlockedShouldSaveBlockedUserWhenUserIsUnblocked() {
        //given
        Integer targetUserId = 436;

        String oldLogin = "login";
        String oldPassword = "d8f6g56sdf";
        String oldFirstName = "john";
        String oldLastName = "doe";
        UserRole oldRole = UserRole.READER;

        User userReturnedByRepository = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, false, oldRole);
        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, true, oldRole);
        //when
        userService.switchUserBlockedById(targetUserId);
        //then
        verify(userRepository, times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void testSwitchUserBlockedShouldSaveUnblockedUserWhenUserIsBlocked() {
        //given
        Integer targetUserId = 436;

        String oldLogin = "login";
        String oldPassword = "d8f6g56sdf";
        String oldFirstName = "john";
        String oldLastName = "doe";
        UserRole oldRole = UserRole.READER;

        User userReturnedByRepository = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, true, oldRole);
        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.of(userReturnedByRepository));

        User expectedUserToBeSaved = new User(targetUserId, oldLogin, oldPassword, oldFirstName, oldLastName, false, oldRole);
        //when
        userService.switchUserBlockedById(targetUserId);
        //then
        verify(userRepository, times(1))
                .save(expectedUserToBeSaved);
    }

    @Test
    public void testSwitchUserBlockedShouldThrowElementNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Integer targetUserId = 436;

        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.switchUserBlockedById(targetUserId))
                .isInstanceOf(ElementNotFoundException.class);
        verify(userRepository, never())
                .save(any());
    }
}
