package com.rhis.jeu_echecs_multijoueurs_back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })

@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @NotBlank
    @Size(max = 20)
    private String prenom;
    @NotBlank
    @Size(max = 20)
    private String nom;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean online = false;

    @Column
    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "whitePlayer")
    private Set<Game> gamesAsWhite = new HashSet<>();

    @OneToMany(mappedBy = "blackPlayer")
    private Set<Game> gamesAsBlack = new HashSet<>();


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public User(String username, String email, String prenom, String nom, String password) {
        this.username = username;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Set<Game> getGamesAsWhite() {
        return gamesAsWhite;
    }

    public void setGamesAsWhite(Set<Game> gamesAsWhite) {
        this.gamesAsWhite = gamesAsWhite;
    }

    public Set<Game> getGamesAsBlack() {
        return gamesAsBlack;
    }

    public void setGamesAsBlack(Set<Game> gamesAsBlack) {
        this.gamesAsBlack = gamesAsBlack;
    }
}
