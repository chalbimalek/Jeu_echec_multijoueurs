package com.rhis.jeu_echecs_multijoueurs_back.service;

import com.rhis.jeu_echecs_multijoueurs_back.model.User;
import com.rhis.jeu_echecs_multijoueurs_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void setUserOnline(String username, boolean online) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setOnline(online);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
    }

    public List<User> getOnlineUsers() {
        return userRepository.findByOnlineTrue();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
