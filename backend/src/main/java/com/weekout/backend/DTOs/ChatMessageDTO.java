package com.weekout.backend.DTOs;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private UUID id;
    private UUID planId;
    private UUID senderId;
    private String senderName;
    private String content;
    private Instant timestamp;
}
