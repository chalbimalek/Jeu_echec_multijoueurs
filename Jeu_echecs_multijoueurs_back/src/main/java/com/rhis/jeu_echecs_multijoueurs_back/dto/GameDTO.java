package com.rhis.jeu_echecs_multijoueurs_back.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long id;
    private String whitePlayer;
    private String blackPlayer;
    private String status;
    private String winner;

    private GameDTO(Builder builder) {
        this.id = builder.id;
        this.whitePlayer = builder.whitePlayer;
        this.blackPlayer = builder.blackPlayer;
        this.status = builder.status;
        this.winner = builder.winner;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
    public static class Builder {
        private Long id;
        private String whitePlayer;
        private String blackPlayer;
        private String status;
        private String winner;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder whitePlayer(String whitePlayer) {
            this.whitePlayer = whitePlayer;
            return this;
        }

        public Builder blackPlayer(String blackPlayer) {
            this.blackPlayer = blackPlayer;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder winner(String winner) {
            this.winner = winner;
            return this;
        }

        public GameDTO build() {
            return new GameDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


}