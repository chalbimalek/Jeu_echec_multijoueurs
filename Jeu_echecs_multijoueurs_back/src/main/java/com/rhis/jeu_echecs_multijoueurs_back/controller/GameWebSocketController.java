package com.rhis.jeu_echecs_multijoueurs_back.controller;

import com.rhis.jeu_echecs_multijoueurs_back.dto.MoveDTO;
import com.rhis.jeu_echecs_multijoueurs_back.dto.MoveResponseDTO;
import com.rhis.jeu_echecs_multijoueurs_back.model.Move;
import com.rhis.jeu_echecs_multijoueurs_back.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameWebSocketController {
    @Autowired
    GameService gameService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/{gameId}/move")
    @SendTo("/topic/game/{gameId}")
    public MoveResponseDTO handleMove(
            @DestinationVariable Long gameId,
            @Payload MoveDTO moveDTO) {

        Move move = gameService.saveMove(
                gameId,
                moveDTO.getFrom(),
                moveDTO.getTo(),
                moveDTO.getPiece(),
                moveDTO.getColor(),
                moveDTO.getCapturedPiece()
        );

        return MoveResponseDTO.builder()
                .moveNumber(move.getMoveNumber())
                .from(move.getFromPosition())
                .to(move.getToPosition())
                .piece(move.getPiece())
                .color(move.getColor())
                .capturedPiece(move.getCapturedPiece())
                .build();
    }

    @MessageMapping("/game/{gameId}/finish")
    public void finishGame(
            @DestinationVariable Long gameId,
            @Payload Map<String, String> payload) {

        String winner = payload.get("winner");
        gameService.finishGame(gameId, winner);

        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/finish",
                Map.of("winner", winner, "gameId", gameId));
    }
}