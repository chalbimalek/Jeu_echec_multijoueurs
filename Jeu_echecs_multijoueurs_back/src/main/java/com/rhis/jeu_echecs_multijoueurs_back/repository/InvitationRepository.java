package com.rhis.jeu_echecs_multijoueurs_back.repository;

import com.rhis.jeu_echecs_multijoueurs_back.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query("SELECT i FROM Invitation i WHERE i.fromUser.username = ?1 AND i.toUser.username = ?2 AND i.status = 'PENDING'")
    Optional<Invitation> findPendingInvitation(String from, String to);

    @Query("SELECT i FROM Invitation i WHERE i.toUser.username = ?1 AND i.status = 'PENDING'")
    List<Invitation> findPendingForUser(String username);
}