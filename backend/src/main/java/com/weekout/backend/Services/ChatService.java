package com.weekout.backend.Services;

import com.weekout.backend.DTOs.ChatMessageDTO;
import com.weekout.backend.Repositories.ChatMessageRepository;
import com.weekout.backend.Model.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Instant;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatMessageRepository repo;

    public ChatService(ChatMessageRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {
        ChatMessage entity = ChatMessage.builder()
                .id(dto.getId() == null ? UUID.randomUUID() : dto.getId())
                .planId(dto.getPlanId())
                .senderId(dto.getSenderId())
                .senderName(dto.getSenderName())
                .content(dto.getContent())
                .timestamp(dto.getTimestamp() == null ? Instant.now() : dto.getTimestamp())
                .build();
        ChatMessage saved = repo.save(entity);
        return mapToDto(saved);
    }

    public List<ChatMessageDTO> getRecentMessages(UUID planId, int limit) {
        List<ChatMessage> rows = repo.findTop100ByPlanIdOrderByTimestampDesc(planId);
        // repository returns newest first; reverse and map, and cap to `limit`
        Collections.reverse(rows);
        return rows.stream()
                .map(this::mapToDto)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<ChatMessageDTO> getAllMessages(UUID planId) {
        return repo.findByPlanIdOrderByTimestampAsc(planId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO mapToDto(ChatMessage m) {
        return ChatMessageDTO.builder()
                .id(m.getId())
                .planId(m.getPlanId())
                .senderId(m.getSenderId())
                .senderName(m.getSenderName())
                .content(m.getContent())
                .timestamp(m.getTimestamp())
                .build();
    }
}
