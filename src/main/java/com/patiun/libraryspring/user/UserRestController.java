package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public void signUpUser(@RequestBody @Valid UserRegistrationDto registrationDto, final HttpServletRequest request) throws ServletException, ServiceException {
        String login = registrationDto.getLogin();
        String password = registrationDto.getPassword();
        String firstName = registrationDto.getFirstName();
        String lastName = registrationDto.getLastName();
        userService.signUp(login, password, firstName, lastName);
        request.login(login, password);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{login}")
    public User getUser(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable Integer id, @RequestBody @Valid UserEditDto editDto) throws ServiceException {
        String newFirstName = editDto.getFirstName();
        String newLastName = editDto.getLastName();
        UserRole newRole = editDto.getRole();
        userService.updateUserById(id, newFirstName, newLastName, newRole);
    }

    @PostMapping("/users/{id}/switch-blocked")
    public void switchUserBlocked(@PathVariable Integer id) throws ServiceException {
        userService.switchUserBlockedById(id);
    }

}
