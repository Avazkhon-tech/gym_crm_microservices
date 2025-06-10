package com.epam.repository;

import com.epam.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
