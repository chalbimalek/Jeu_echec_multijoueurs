package com.rhis.jeu_echecs_multijoueurs_back.repository;

import com.rhis.jeu_echecs_multijoueurs_back.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByGameIdOrderByMoveNumberAsc(Long gameId);
}