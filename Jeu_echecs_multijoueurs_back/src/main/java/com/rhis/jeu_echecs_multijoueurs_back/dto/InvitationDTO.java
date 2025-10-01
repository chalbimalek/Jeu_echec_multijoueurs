package com.rhis.jeu_echecs_multijoueurs_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class InvitationDTO {
    private Long id;
    private String fromUsername;
    private String toUsername;
    private String status;

    public InvitationDTO(Long id, String fromUsername, String toUsername, String status) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Builder {
        private Long id;
        private String fromUsername;
        private String toUsername;
        private String status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fromUsername(String fromUsername) {
            this.fromUsername = fromUsername;
            return this;
        }

        public Builder toUsername(String toUsername) {
            this.toUsername = toUsername;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public InvitationDTO build() {
            return new InvitationDTO(id, fromUsername, toUsername, status);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}