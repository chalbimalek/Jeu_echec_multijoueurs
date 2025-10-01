package com.rhis.jeu_echecs_multijoueurs_back.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MoveResponseDTO {
    private int moveNumber;
    private String from;
    private String to;
    private String piece;
    private String color;
    private String capturedPiece;


    private MoveResponseDTO(Builder builder) {
        this.moveNumber = builder.moveNumber;
        this.from = builder.from;
        this.to = builder.to;
        this.piece = builder.piece;
        this.color = builder.color;
        this.capturedPiece = builder.capturedPiece;
    }


    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(String capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    public static class Builder {
        private int moveNumber;
        private String from;
        private String to;
        private String piece;
        private String color;
        private String capturedPiece;

        public Builder moveNumber(int moveNumber) {
            this.moveNumber = moveNumber;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
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

        public Builder capturedPiece(String capturedPiece) {
            this.capturedPiece = capturedPiece;
            return this;
        }

        public MoveResponseDTO build() {
            return new MoveResponseDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
