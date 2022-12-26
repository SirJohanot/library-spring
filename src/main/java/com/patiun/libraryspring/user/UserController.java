package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.utility.Paginator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/sign-up")
    public String signUp(@RequestParam String login, @RequestParam String password, @RequestParam("confirmed-password") String confirmedPassword, @RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName, final Model model, final HttpServletRequest request) throws ServletException {
        try {
            userService.signUp(login, password, confirmedPassword, firstName, lastName);
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

    @GetMapping("/edit-user-page/{id}")
    public String editUserPage(@PathVariable Integer id, final Model model) throws ServiceException {
        getUserByIdAndAddToModel(id, model);

        return "editUser";
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestParam Integer id, @RequestParam("first-name") String firstName, @RequestParam("last-name") String lastName, @RequestParam UserRole role) throws ServiceException {
        userService.updateUserById(id, firstName, lastName, role);

        return "redirect:/user/" + getLoginOfUserById(id);
    }

    @PostMapping("/switch-user-blocked")
    public String switchUserBlocked(@RequestParam Integer id) throws ServiceException {
        userService.switchUserBlockedById(id);
        return "redirect:/user/" + getLoginOfUserById(id);
    }

    private void getUserByIdAndAddToModel(Integer id, final Model model) throws ServiceException {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);
    }

    private String getLoginOfUserById(Integer id) throws ServiceException {
        User user = userService.getUserById(id);

        return user.getLogin();
    }

}
