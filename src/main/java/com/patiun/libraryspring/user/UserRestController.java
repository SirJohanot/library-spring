package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true)
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
