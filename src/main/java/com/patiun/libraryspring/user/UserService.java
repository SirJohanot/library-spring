package com.patiun.libraryspring.user;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

    public void signUp(String login, String password, String confirmedPassword, String firstName, String lastName) {
        if (userRepository.findByLogin(login) != null) {
            throw new ServiceException("A user with such login already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);

        userRepository.save(new User(login, encodedPassword, firstName, lastName, false, UserRole.READER));
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }

    public User getUserById(Integer id) {
        return getExistingUserById(id);
    }

    public void switchUserBlockedById(Integer id) {
        Optional<User> foundUserOptional = userRepository.findById(id);
        if (foundUserOptional.isEmpty()) {
            throw new NoSuchElementException("Could not find user by id = " + id);
        }
        User foundUser = foundUserOptional.get();

        Boolean currentUserBlocked = foundUser.getBlocked();
        foundUser.setBlocked(!currentUserBlocked);

        userRepository.save(foundUser);
    }

    private User getExistingUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("Could not find a user by id = " + id);
        }
        return userOptional.get();
    }

}
