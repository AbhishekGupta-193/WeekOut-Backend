package com.weekout.backend.Controllers;

import com.weekout.backend.DTOs.ChatMessageDTO;
import com.weekout.backend.Services.ChatService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    /**
     * Client sends to: /app/chat/{planId}
     * Server will save and broadcast to: /topic/plans/{planId}
     */
    @MessageMapping("/chat/{planId}")
    public void onMessage(@DestinationVariable String planId, @Payload ChatMessageDTO incoming) {
        // ensure planId matches payload
        incoming.setPlanId(UUID.fromString(planId));

        // persist
        ChatMessageDTO saved = chatService.saveMessage(incoming);

        // broadcast to subscribers of this plan
        messagingTemplate.convertAndSend("/topic/plans/" + planId, saved);
    }
}
