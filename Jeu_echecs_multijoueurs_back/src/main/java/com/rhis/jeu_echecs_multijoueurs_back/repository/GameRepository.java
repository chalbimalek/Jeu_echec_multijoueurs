package com.rhis.jeu_echecs_multijoueurs_back.repository;

import com.rhis.jeu_echecs_multijoueurs_back.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g WHERE (g.whitePlayer.username = ?1 OR g.blackPlayer.username = ?1) AND g.status = 'ACTIVE'")
    Optional<Game> findActiveGameForUser(String username);

    @Query("SELECT g FROM Game g WHERE g.whitePlayer.username = ?1 OR g.blackPlayer.username = ?1")
    List<Game> findByUsername(String username);
}