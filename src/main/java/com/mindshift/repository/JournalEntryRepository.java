package com.mindshift.repository;

import com.mindshift.model.JournalEntry;
import com.mindshift.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserOrderByCreatedAtDesc(User user);
    List<JournalEntry> findByUserIdOrderByCreatedAtAsc(Long userId);
}
