package com.mindshift.repository;

import com.mindshift.model.ChatMessage;
import com.mindshift.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserOrderByCreatedAtAsc(User user);
    List<ChatMessage> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
