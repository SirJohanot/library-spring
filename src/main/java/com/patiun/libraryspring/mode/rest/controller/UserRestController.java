package com.patiun.libraryspring.mode.rest.controller;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.mode.rest.configuration.RestSecurityConfig;
import com.patiun.libraryspring.user.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = RestSecurityConfig.FRONT_END_URL)
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
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
    public Map<String, UserRole[]> authenticate(final Authentication authentication) {
        String login = authentication.getName();
        User authenticatingUser = userService.getUserByLogin(login);
        UserRole authenticatingUserRole = authenticatingUser.getRole();
        return Map.of("roles", new UserRole[]{authenticatingUserRole});
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
    public void updateUser(@PathVariable Integer id, @RequestBody @Valid UserEditDto editDto) throws ServiceException {
        String newFirstName = editDto.getFirstName();
        String newLastName = editDto.getLastName();
        UserRole newRole = editDto.getRole();
        userService.updateUserById(id, newFirstName, newLastName, newRole);
    }

    @PutMapping("{id}/switch-blocked")
    public void switchUserBlocked(@PathVariable Integer id) throws ServiceException {
        userService.switchUserBlockedById(id);
    }

}
