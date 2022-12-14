package com.patiun.libraryspring.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

}
