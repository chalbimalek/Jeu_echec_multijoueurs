package com.rhis.jeu_echecs_multijoueurs_back.service;

import com.rhis.jeu_echecs_multijoueurs_back.model.Invitation;
import com.rhis.jeu_echecs_multijoueurs_back.model.InvitationStatus;
import com.rhis.jeu_echecs_multijoueurs_back.model.User;
import com.rhis.jeu_echecs_multijoueurs_back.repository.InvitationRepository;
import com.rhis.jeu_echecs_multijoueurs_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Invitation createInvitation(String fromUsername, String toUsername) {
        User fromUser = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new RuntimeException("From user not found"));
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("To user not found"));

        Optional<Invitation> existing = invitationRepository
                .findPendingInvitation(fromUsername, toUsername);
        if (existing.isPresent()) {
            throw new RuntimeException("Invitation already sent");
        }

        Invitation invitation = Invitation.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(InvitationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return invitationRepository.save(invitation);
    }

    @Transactional
    public Invitation acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setStatus(InvitationStatus.ACCEPTED);
        return invitationRepository.save(invitation);
    }

    @Transactional
    public Invitation rejectInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setStatus(InvitationStatus.REJECTED);
        return invitationRepository.save(invitation);
    }

    public List<Invitation> getPendingInvitations(String username) {
        return invitationRepository.findPendingForUser(username);
    }
}
