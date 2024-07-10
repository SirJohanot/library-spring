package com.patiun.libraryspring.configuration;

import com.patiun.libraryspring.exception.ServiceException;
import com.patiun.libraryspring.user.User;
import com.patiun.libraryspring.user.UserRole;
import com.patiun.libraryspring.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceholderAdminGenerator {

    @Value("${placeholder-admin.login:admin}")
    private String placeholderAdminLogin;

    @Value("${placeholder-admin.password:admin}")
    private String placeholderAdminPassword;

    @Value("${placeholder-admin.first-name:placeholder}")
    private String placeholderAdminFirstName;

    @Value("${placeholder-admin.last-name:admin}")
    private String placeholderAdminLastName;

    private final UserService userService;

    @Autowired
    public PlaceholderAdminGenerator(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void generatePlaceholderAdmin() throws ServiceException {
        List<User> existingAdmins = userService.getAllAdmins();
        if (existingAdmins.isEmpty()) {
            User savedAdmin = userService.createUserDirectly(placeholderAdminLogin, placeholderAdminPassword, placeholderAdminFirstName, placeholderAdminLastName);
            Integer savedAdminId = savedAdmin.getId();
            userService.updateUserById(savedAdminId, placeholderAdminFirstName, placeholderAdminLastName, UserRole.ADMIN);
        }
    }
}
