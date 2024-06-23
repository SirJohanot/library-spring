package com.patiun.libraryspring.user;

import com.patiun.libraryspring.configuration.JwtService;
import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.exception.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserRestController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public void signUpUser(@RequestBody @Valid UserRegistrationDto registrationDto) throws ServiceException {
        String login = registrationDto.getLogin();
        String password = registrationDto.getPassword();
        String firstName = registrationDto.getFirstName();
        String lastName = registrationDto.getLastName();
        userService.signUp(login, password, firstName, lastName);
    }

    @GetMapping("auth")
    public AuthenticationResponseDto authenticate(final Authentication authentication) {
        String login = authentication.getName();
        User authenticatingUser = userService.getUserByLogin(login);
        UserRole authenticatingUserRole = authenticatingUser.getRole();
        return new AuthenticationResponseDto(jwtService.generateToken(login), new UserRole[]{authenticatingUserRole});
    }

    @GetMapping
    public List<User> readAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{login}")
    public User readUser(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @PutMapping("{id}")
    public void updateUser(@PathVariable Integer id, @RequestBody @Valid UserEditDto editDto) {
        String newFirstName = editDto.getFirstName();
        String newLastName = editDto.getLastName();
        UserRole newRole = editDto.getRole();
        userService.updateUserById(id, newFirstName, newLastName, newRole);
    }

    @PatchMapping("{id}/switch-blocked")
    public void switchUserBlocked(@PathVariable Integer id) {
        userService.switchUserBlockedById(id);
    }

    @PatchMapping("{id}/change-password")
    public void changePassword(@PathVariable Integer targetUserId, @RequestBody @Valid NewPasswordDto passwordDto, final Authentication authentication) throws UnauthorizedException {
        String login = authentication.getName();
        User authenticatedUser = userService.getUserByLogin(login);
        Integer authenticatedUserId = authenticatedUser.getId();
        if (!Objects.equals(targetUserId, authenticatedUserId)) {
            throw new UnauthorizedException("You cannot change another user's password");
        }

        String newPassword = passwordDto.getPassword();
        userService.changeUserPasswordById(targetUserId, newPassword);
    }

}
