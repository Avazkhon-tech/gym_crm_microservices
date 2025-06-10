package com.epam.repository.impl;

import com.epam.model.User;
import com.epam.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> implements UserRepository {

    public boolean existsByUsername(String username) {

        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();

        return count > 0;
    }

    public Optional<User> findByUsername(String username) {
        User user = entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();

        return Optional.ofNullable(user);
    }
}
