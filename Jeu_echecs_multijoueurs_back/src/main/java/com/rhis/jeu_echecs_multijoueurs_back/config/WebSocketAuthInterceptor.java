package com.rhis.jeu_echecs_multijoueurs_back.config;

import com.rhis.jeu_echecs_multijoueurs_back.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtil; // Ou votre service JWT

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = accessor.getFirstNativeHeader("Authorization");

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7);
                String username = jwtUtil.getUserNameFromJwtToken(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                accessor.setUser(authentication);

                System.out.println("=== WebSocket CONNECT - User authentifié: " + username);  // LOG IMPORTANT
            }
        }

        System.out.println("=== WebSocket Message - Command: " + accessor.getCommand() +
                ", User: " + (accessor.getUser() != null ? accessor.getUser().getName() : "null"));

        return message;
    }
}