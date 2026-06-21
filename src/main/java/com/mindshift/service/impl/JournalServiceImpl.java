package com.mindshift.service.impl;

import com.mindshift.dto.request.JournalRequest;
import com.mindshift.dto.response.JournalResponse;
import com.mindshift.event.ScoreRecalculationEvent;
import com.mindshift.exception.BadRequestException;
import com.mindshift.exception.ResourceNotFoundException;
import com.mindshift.model.JournalEntry;
import com.mindshift.model.User;
import com.mindshift.repository.JournalEntryRepository;
import com.mindshift.service.JournalService;
import com.mindshift.util.classifier.ThoughtClassifierStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {

    private final JournalEntryRepository journalRepository;
    private final ThoughtClassifierStrategy classifier; // Strategy injected by Spring
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public JournalResponse create(User user, JournalRequest request) {
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .text(request.getText())
                .intensity(request.getIntensity())
                .tags(request.getTags())
                .category(classifier.classify(request.getText()))
                .build();
        journalRepository.save(entry);

        eventPublisher.publishEvent(new ScoreRecalculationEvent(user.getId()));
        return JournalResponse.from(entry);
    }

    @Override
    public List<JournalResponse> getAll(User user) {
        return journalRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(JournalResponse::from).toList();
    }

    @Override
    @Transactional
    public void delete(User user, Long entryId) {
        JournalEntry entry = journalRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found"));
        if (!entry.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot delete another user's entry");
        }
        journalRepository.delete(entry);
    }
}
