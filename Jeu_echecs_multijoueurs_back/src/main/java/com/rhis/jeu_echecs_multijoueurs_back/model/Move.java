package com.rhis.jeu_echecs_multijoueurs_back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "moves")
@Data
@AllArgsConstructor
@Builder
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private int moveNumber;

    @Column(nullable = false)
    private String fromPosition;

    @Column(nullable = false)
    private String toPosition;

    @Column
    private String piece;

    @Column
    private String color;

    @Column
    private LocalDateTime timestamp;

    @Column
    private String capturedPiece;

    private Move(Builder builder) {
        this.id = builder.id;
        this.game = builder.game;
        this.moveNumber = builder.moveNumber;
        this.fromPosition = builder.fromPosition;
        this.toPosition = builder.toPosition;
        this.piece = builder.piece;
        this.color = builder.color;
        this.timestamp = builder.timestamp;
        this.capturedPiece = builder.capturedPiece;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(String fromPosition) {
        this.fromPosition = fromPosition;
    }

    public String getToPosition() {
        return toPosition;
    }

    public void setToPosition(String toPosition) {
        this.toPosition = toPosition;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(String capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    public static class Builder {
        private Long id;
        private Game game;
        private int moveNumber;
        private String fromPosition;
        private String toPosition;
        private String piece;
        private String color;
        private LocalDateTime timestamp;
        private String capturedPiece;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder game(Game game) {
            this.game = game;
            return this;
        }

        public Builder moveNumber(int moveNumber) {
            this.moveNumber = moveNumber;
            return this;
        }

        public Builder fromPosition(String fromPosition) {
            this.fromPosition = fromPosition;
            return this;
        }

        public Builder toPosition(String toPosition) {
            this.toPosition = toPosition;
            return this;
        }

        public Builder piece(String piece) {
            this.piece = piece;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder capturedPiece(String capturedPiece) {
            this.capturedPiece = capturedPiece;
            return this;
        }

        public Move build() {
            return new Move(this);
        }
    }

    public Move() {
    }

    public static Builder builder() {
        return new Builder();
    }
}
