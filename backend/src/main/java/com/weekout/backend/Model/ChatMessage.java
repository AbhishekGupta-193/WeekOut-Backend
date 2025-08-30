package com.weekout.backend.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // The plan this chat belongs to
    @Column(name = "plan_id", columnDefinition = "uuid", nullable = false)
    private UUID planId;

    @Column(name = "sender_id", columnDefinition = "uuid", nullable = false)
    private UUID senderId;

    private String senderName;

    @Column(columnDefinition = "TEXT")
    private String content;

    // timestamp stored as instant
    @Column(nullable = false)
    private Instant timestamp;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (timestamp == null) timestamp = Instant.now();
    }
}
