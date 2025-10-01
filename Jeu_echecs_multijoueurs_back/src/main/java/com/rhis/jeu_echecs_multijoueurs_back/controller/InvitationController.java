package com.rhis.jeu_echecs_multijoueurs_back.controller;

import com.rhis.jeu_echecs_multijoueurs_back.dto.GameDTO;
import com.rhis.jeu_echecs_multijoueurs_back.dto.InvitationDTO;
import com.rhis.jeu_echecs_multijoueurs_back.model.Game;
import com.rhis.jeu_echecs_multijoueurs_back.model.Invitation;
import com.rhis.jeu_echecs_multijoueurs_back.service.GameService;
import com.rhis.jeu_echecs_multijoueurs_back.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invitations")
@CrossOrigin(origins = "http://localhost:4200")
public class InvitationController {
    @Autowired
    InvitationService invitationService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    GameService gameService;

    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String toUsername = request.get("toUsername");
            System.out.println("=== Envoi invitation de " + userDetails.getUsername() + " vers " + toUsername);

            Invitation invitation = invitationService.createInvitation(
                    userDetails.getUsername(), toUsername);

            InvitationDTO dto = InvitationDTO.builder()
                    .id(invitation.getId())
                    .fromUsername(invitation.getFromUser().getUsername())
                    .toUsername(invitation.getToUser().getUsername())
                    .status(invitation.getStatus().name())
                    .build();

            System.out.println("=== Tentative d'envoi WebSocket vers: " + toUsername);
            System.out.println("=== DTO à envoyer: " + dto);

            // Send WebSocket notification
            messagingTemplate.convertAndSendToUser(
                    toUsername,
                    "/queue/invitations",
                    dto
            );

            System.out.println("=== Message WebSocket envoyé avec succès");

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/pending")
    public ResponseEntity<List<InvitationDTO>> getPendingInvitations(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<InvitationDTO> invitations = invitationService
                .getPendingInvitations(userDetails.getUsername()).stream()
                .map(i -> InvitationDTO.builder()
                        .id(i.getId())
                        .fromUsername(i.getFromUser().getUsername())
                        .toUsername(i.getToUser().getUsername())
                        .status(i.getStatus().name())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(invitations);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Invitation invitation = invitationService.acceptInvitation(id);

            // Créer la partie immédiatement
            Game game = gameService.createGame(
                    invitation.getFromUser().getUsername(),
                    invitation.getToUser().getUsername()
            );

            GameDTO gameDTO = GameDTO.builder()
                    .id(game.getId())
                    .whitePlayer(game.getWhitePlayer().getUsername())
                    .blackPlayer(game.getBlackPlayer().getUsername())
                    .status(game.getStatus().name())
                    .build();

            // Notifier les DEUX joueurs via WebSocket
            messagingTemplate.convertAndSendToUser(
                    invitation.getFromUser().getUsername(),
                    "/queue/game-start",
                    gameDTO);

            messagingTemplate.convertAndSendToUser(
                    invitation.getToUser().getUsername(),
                    "/queue/game-start",
                    gameDTO);

            return ResponseEntity.ok(gameDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInvitation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Invitation invitation = invitationService.rejectInvitation(id);

            // Notify sender
            messagingTemplate.convertAndSendToUser(
                    invitation.getFromUser().getUsername(),
                    "/queue/invitation-response",
                    Map.of("invitationId", id, "accepted", false));

            return ResponseEntity.ok(Map.of("message", "Invitation rejected"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
