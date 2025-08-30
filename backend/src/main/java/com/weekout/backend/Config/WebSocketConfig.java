package com.weekout.backend.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // client connects to /ws (SockJS fallback enabled)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")   // development: allow all origins; restrict for prod
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // messages from server to clients prefixed with /topic
        registry.enableSimpleBroker("/topic");
        // messages from client to server should be sent to /app/*
        registry.setApplicationDestinationPrefixes("/app");
    }
}

