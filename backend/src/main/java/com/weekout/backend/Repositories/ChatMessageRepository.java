package com.weekout.backend.Repositories;

import com.weekout.backend.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    // return most recent messages (desc); we'll reverse to ascending when returning
    List<ChatMessage> findTop100ByPlanIdOrderByTimestampDesc(UUID planId);

    // full history ascending (optional)
    List<ChatMessage> findByPlanIdOrderByTimestampAsc(UUID planId);
}
