package com.patiun.libraryspring.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    List<User> findByRoleIs(UserRole role);
}
