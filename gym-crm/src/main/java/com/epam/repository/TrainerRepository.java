package com.epam.repository;


import com.epam.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("""
            SELECT COUNT(t) > 0 FROM Trainer t
            WHERE t.user.username = :username
            """)
    Boolean existsByUsername(@Param("username") String username);


    @Query("""
            SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username
            """)
    Optional<Trainer> findByUsername(@Param("username") String username);


    @Query("""
            SELECT t FROM Trainer t JOIN t.user u WHERE u.username IN :usernames
            """)
    List<Trainer> findAllByUsernames(@Param("usernames") List<String> usernames);

}
