package com.weekout.backend.Controllers;

import com.weekout.backend.DTOs.ChatMessageDTO;
import com.weekout.backend.Services.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "*") // adjust for prod
public class ChatRestController {

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    // GET /api/chats/{planId}?limit=100
    @GetMapping("/{planId}")
    public List<ChatMessageDTO> getChatHistory(
            @PathVariable UUID planId,
            @RequestParam(defaultValue = "100") int limit
    ) {
        return chatService.getRecentMessages(planId, limit);
    }
}
