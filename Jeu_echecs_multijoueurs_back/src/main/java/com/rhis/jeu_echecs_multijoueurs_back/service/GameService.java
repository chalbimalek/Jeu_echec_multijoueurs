package com.rhis.jeu_echecs_multijoueurs_back.service;

import com.rhis.jeu_echecs_multijoueurs_back.model.Game;
import com.rhis.jeu_echecs_multijoueurs_back.model.GameStatus;
import com.rhis.jeu_echecs_multijoueurs_back.model.Move;
import com.rhis.jeu_echecs_multijoueurs_back.model.User;
import com.rhis.jeu_echecs_multijoueurs_back.repository.GameRepository;
import com.rhis.jeu_echecs_multijoueurs_back.repository.MoveRepository;
import com.rhis.jeu_echecs_multijoueurs_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;
    @Autowired
     MoveRepository moveRepository;
    @Autowired
     UserRepository userRepository;

    @Transactional
    public Game createGame(String whiteUsername, String blackUsername) {
        User whitePlayer = userRepository.findByUsername(whiteUsername)
                .orElseThrow(() -> new RuntimeException("White player not found"));
        User blackPlayer = userRepository.findByUsername(blackUsername)
                .orElseThrow(() -> new RuntimeException("Black player not found"));

        Game game = Game.builder()
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .status(GameStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return gameRepository.save(game);
    }

    @Transactional
    public Move saveMove(Long gameId, String from, String to,
                         String piece, String color, String capturedPiece) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        int moveNumber = game.getMoves().size() + 1;

        Move move = Move.builder()
                .game(game)
                .moveNumber(moveNumber)
                .fromPosition(from)
                .toPosition(to)
                .piece(piece)
                .color(color)
                .capturedPiece(capturedPiece)
                .timestamp(LocalDateTime.now())
                .build();

        game.setUpdatedAt(LocalDateTime.now());
        gameRepository.save(game);

        return moveRepository.save(move);
    }

    public List<Move> getGameMoves(Long gameId) {
        return moveRepository.findByGameIdOrderByMoveNumberAsc(gameId);
    }

    public Optional<Game> getActiveGameForUser(String username) {
        return gameRepository.findActiveGameForUser(username);
    }

    public List<Game> getUserGames(String username) {
        return gameRepository.findByUsername(username);
    }

    @Transactional
    public void finishGame(Long gameId, String winner) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setStatus(GameStatus.FINISHED);
        game.setWinner(winner);
        game.setUpdatedAt(LocalDateTime.now());
        gameRepository.save(game);
    }
}