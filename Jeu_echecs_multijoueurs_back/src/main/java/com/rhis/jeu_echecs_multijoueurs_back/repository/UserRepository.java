package com.rhis.jeu_echecs_multijoueurs_back.repository;

import com.rhis.jeu_echecs_multijoueurs_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByOnlineTrue();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}