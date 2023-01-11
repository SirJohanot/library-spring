package com.patiun.libraryspring.user;

import com.patiun.libraryspring.exception.ElementNotFoundException;
import com.patiun.libraryspring.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByLogin(username);
        if (foundUser == null) {
            throw new UsernameNotFoundException("Could not find a user by username '" + username + "'");
        }
        return foundUser;
    }

    public void signUp(String login, String password, String firstName, String lastName) throws ServiceException {
        if (userRepository.findByLogin(login) != null) {
            throw new ServiceException("A user with such login already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);

        userRepository.save(new User(null, login, encodedPassword, firstName, lastName, false, UserRole.READER));
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }

    public User getUserById(Integer id) {
        return getExistingUserById(id);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void updateUserById(Integer id, String firstName, String lastName, UserRole role) throws ServiceException {
        User targetUser = getExistingUserById(id);

        targetUser.setFirstName(firstName);
        targetUser.setLastName(lastName);
        targetUser.setRole(role);

        userRepository.save(targetUser);
    }

    public void switchUserBlockedById(Integer id) throws ServiceException {
        Optional<User> foundUserOptional = userRepository.findById(id);
        if (foundUserOptional.isEmpty()) {
            throw new ServiceException("Could not find user by id = " + id);
        }
        User foundUser = foundUserOptional.get();

        Boolean currentUserBlocked = foundUser.getBlocked();
        foundUser.setBlocked(!currentUserBlocked);

        userRepository.save(foundUser);
    }

    private User getExistingUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ElementNotFoundException("Could not find a user by id = " + id);
        }
        return userOptional.get();
    }

}
