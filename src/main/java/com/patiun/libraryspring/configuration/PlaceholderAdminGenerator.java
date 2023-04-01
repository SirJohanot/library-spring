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
    private String PLACEHOLDER_ADMIN_LOGIN;

    @Value("${placeholder-admin.password:admin}")
    private String PLACEHOLDER_ADMIN_PASSWORD;

    @Value("${placeholder-admin.first-name:placeholder}")
    private String PLACEHOLDER_ADMIN_FIRST_NAME;

    @Value("${placeholder-admin.last-name:admin}")
    private String PLACEHOLDER_ADMIN_LAST_NAME;

    private final UserService userService;

    @Autowired
    public PlaceholderAdminGenerator(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void generatePlaceholderAdmin() throws ServiceException {
        List<User> existingAdmins = userService.getAllAdmins();
        if (existingAdmins.isEmpty()) {
            User savedAdmin = userService.signUp(PLACEHOLDER_ADMIN_LOGIN, PLACEHOLDER_ADMIN_PASSWORD, PLACEHOLDER_ADMIN_FIRST_NAME, PLACEHOLDER_ADMIN_LAST_NAME);
            Integer savedAdminId = savedAdmin.getId();
            userService.updateUserById(savedAdminId, PLACEHOLDER_ADMIN_FIRST_NAME, PLACEHOLDER_ADMIN_LAST_NAME, UserRole.ADMIN);
        }
    }
}
