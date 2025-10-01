package com.rhis.jeu_echecs_multijoueurs_back.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "games")
@Data
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "white_player_id")
    private User whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id")
    private User blackPlayer;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column
    private String winner;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Move> moves = new HashSet<>();


    private Game(Builder builder) {
        this.id = builder.id;
        this.whitePlayer = builder.whitePlayer;
        this.blackPlayer = builder.blackPlayer;
        this.status = builder.status;
        this.winner = builder.winner;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.moves = builder.moves != null ? builder.moves : new HashSet<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(User whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public User getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(User blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }
    public static class Builder {
        private Long id;
        private User whitePlayer;
        private User blackPlayer;
        private GameStatus status;
        private String winner;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Set<Move> moves;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder whitePlayer(User whitePlayer) {
            this.whitePlayer = whitePlayer;
            return this;
        }

        public Builder blackPlayer(User blackPlayer) {
            this.blackPlayer = blackPlayer;
            return this;
        }

        public Builder status(GameStatus status) {
            this.status = status;
            return this;
        }

        public Builder winner(String winner) {
            this.winner = winner;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder moves(Set<Move> moves) {
            this.moves = moves;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }

    public Game() {
    }

    public static Builder builder() {
        return new Builder();
    }
}