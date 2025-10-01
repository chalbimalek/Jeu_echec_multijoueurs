package com.rhis.jeu_echecs_multijoueurs_back.controller;

import com.rhis.jeu_echecs_multijoueurs_back.dto.UserDTO;
import com.rhis.jeu_echecs_multijoueurs_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/online")
    public ResponseEntity<List<UserDTO>> getOnlineUsers(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<UserDTO> users = userService.getOnlineUsers().stream()
                .filter(u -> !u.getUsername().equals(userDetails.getUsername()))
                .map(u -> UserDTO.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .online(u.isOnline())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}