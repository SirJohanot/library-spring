package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.utility.Paginator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private static final Integer USERS_PER_PAGE = 5;

    private final UserService userService;
    private final Paginator<User> userPaginator;

    @Autowired
    public UserController(UserService userService, Paginator<User> userPaginator) {
        this.userService = userService;
        this.userPaginator = userPaginator;
    }

    @GetMapping("/sign-up")
    public String signUpPage(final Model model) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        model.addAttribute("registrationDto", registrationDto);
        return "signUp";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("registrationDto") @Valid UserRegistrationDto registrationDto, final BindingResult result, final Model model, final HttpServletRequest request) throws ServletException {
        if (result.hasErrors()) {
            bindErrorToModelAttribute(result, model);
            return "signUp";
        }

        String login = registrationDto.getLogin();
        String password = registrationDto.getPassword();
        String firstName = registrationDto.getFirstName();
        String lastName = registrationDto.getLastName();
        try {
            userService.signUp(login, password, firstName, lastName);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "signUp";
        }
        request.login(login, password);
        return "redirect:/";
    }

    @GetMapping({"/users/", "/users/{page}"})
    public String users(@PathVariable("page") Optional<Integer> optionalPage, final Model model) {
        int page = optionalPage.orElse(1);

        List<User> users = userService.getAllUsers();

        List<User> usersOfTheTargetPage = userPaginator.getEntitiesOfPage(users, page, USERS_PER_PAGE);
        int pagesNeededToContainAllUsers = userPaginator.getNumberOfPagesToContainEntities(users, USERS_PER_PAGE);
        int acceptableTargetPage = userPaginator.getClosestAcceptableTargetPage(users, page, USERS_PER_PAGE);

        model.addAttribute("users", usersOfTheTargetPage);
        model.addAttribute("currentPage", acceptableTargetPage);
        model.addAttribute("maxPage", pagesNeededToContainAllUsers);

        return "users";
    }

    @GetMapping("/user/{login}")
    public String user(@PathVariable String login, final Model model) throws ServiceException {
        User user = userService.getUserByLogin(login);

        model.addAttribute("user", user);

        return "user";
    }

    @GetMapping("/edit-user/{id}")
    public String editUserPage(@PathVariable Integer id, final Model model) {
        getUserByIdAndAddToModel(id, model);

        return "editUser";
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestParam Integer id, @ModelAttribute("user") @Valid UserEditDto editDto, final BindingResult result, final Model model) throws ServiceException {
        if (result.hasErrors()) {
            bindErrorToModelAttribute(result, model);
            return editUserPage(id, model);
        }

        String newFirstName = editDto.getFirstName();
        String newLastName = editDto.getLastName();
        UserRole newRole = editDto.getRole();
        userService.updateUserById(id, newFirstName, newLastName, newRole);

        return "redirect:/user/" + getLoginOfUserById(id);
    }

    @PostMapping("/switch-user-blocked")
    public String switchUserBlocked(@RequestParam Integer id) throws ServiceException {
        userService.switchUserBlockedById(id);
        return "redirect:/user/" + getLoginOfUserById(id);
    }

    private void getUserByIdAndAddToModel(Integer id, final Model model) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
    }

    private String getLoginOfUserById(Integer id) {
        User user = userService.getUserById(id);

        return user.getLogin();
    }

    private void bindErrorToModelAttribute(final BindingResult result, final Model model) {
        List<ObjectError> allErrors = result.getAllErrors();
        ObjectError firstError = allErrors.get(0);
        String errorMessage = firstError.getDefaultMessage();
        model.addAttribute("error", errorMessage);
    }

}
