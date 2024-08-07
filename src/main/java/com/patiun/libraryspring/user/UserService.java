package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ServiceException;

import java.util.List;

public interface UserService {

    User signUp(String login, String password, String firstName, String lastName) throws ServiceException;

    User createUserDirectly(String login, String password, String firstName, String lastName) throws ServiceException;

    List<User> getAllUsers();

    List<User> getAllAdmins();

    User getUserById(Integer id);

    User getUserByLogin(String login);

    void updateUserById(Integer id, String firstName, String lastName, UserRole role);

    void switchUserBlockedById(Integer id);

    void enableUserById(Integer id);

    void changeUserPasswordById(Integer id, String newPassword);

    void activateUserById(Integer id);
}
