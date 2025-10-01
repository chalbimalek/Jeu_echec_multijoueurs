package com.rhis.jeu_echecs_multijoueurs_back.dto;

import com.rhis.jeu_echecs_multijoueurs_back.model.User;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String prenom;
    private String nom;
    private String email;
    private boolean online;


    // Constructeur complet pour le builder
    public UserDTO(Long id, String username, String prenom, String nom, String email, boolean online) {
        this.id = id;
        this.username = username;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.online = online;
    }



    public static class Builder {
        private Long id;
        private String username;
        private String prenom;
        private String nom;
        private String email;
        private boolean online;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder prenom(String prenom) { this.prenom = prenom; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder online(boolean online) { this.online = online; return this; }

        public UserDTO build() {
            return new UserDTO(id, username, prenom, nom, email, online);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
