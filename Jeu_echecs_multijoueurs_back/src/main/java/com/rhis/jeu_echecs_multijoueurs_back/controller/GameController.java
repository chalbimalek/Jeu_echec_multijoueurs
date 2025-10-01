package com.rhis.jeu_echecs_multijoueurs_back.controller;

import com.rhis.jeu_echecs_multijoueurs_back.dto.GameDTO;
import com.rhis.jeu_echecs_multijoueurs_back.dto.MoveResponseDTO;
import com.rhis.jeu_echecs_multijoueurs_back.model.Game;
import com.rhis.jeu_echecs_multijoueurs_back.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {
    @Autowired
    GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<?> createGame(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String opponent = request.get("opponent");
            Game game = gameService.createGame(userDetails.getUsername(), opponent);

            GameDTO dto = GameDTO.builder()
                    .id(game.getId())
                    .whitePlayer(game.getWhitePlayer().getUsername())
                    .blackPlayer(game.getBlackPlayer().getUsername())
                    .status(game.getStatus().name())
                    .build();

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveGame(
            @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Game> game = gameService.getActiveGameForUser(userDetails.getUsername());
        if (game.isPresent()) {
            Game g = game.get();
            GameDTO dto = GameDTO.builder()
                    .id(g.getId())
                    .whitePlayer(g.getWhitePlayer().getUsername())
                    .blackPlayer(g.getBlackPlayer().getUsername())
                    .status(g.getStatus().name())
                    .winner(g.getWinner())  // AJOUTER cette ligne

                    .build();
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.ok(Map.of("message", "No active game"));
    }

    @GetMapping("/{gameId}/moves")
    public ResponseEntity<List<MoveResponseDTO>> getGameMoves(@PathVariable Long gameId) {
        List<MoveResponseDTO> moves = gameService.getGameMoves(gameId).stream()
                .map(m -> MoveResponseDTO.builder()
                        .moveNumber(m.getMoveNumber())
                        .from(m.getFromPosition())
                        .to(m.getToPosition())
                        .piece(m.getPiece())
                        .color(m.getColor())
                        .capturedPiece(m.getCapturedPiece())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(moves);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameDTO>> getUserGames(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<GameDTO> games = gameService.getUserGames(userDetails.getUsername()).stream()
                .map(g -> GameDTO.builder()
                        .id(g.getId())
                        .whitePlayer(g.getWhitePlayer().getUsername())
                        .blackPlayer(g.getBlackPlayer().getUsername())
                        .status(g.getStatus().name())
                        .winner(g.getWinner())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(games);
    }
}

