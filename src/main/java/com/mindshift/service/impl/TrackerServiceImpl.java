package com.mindshift.service.impl;

import com.mindshift.dto.request.TrackerRequest;
import com.mindshift.dto.response.TrackerResponse;
import com.mindshift.event.ScoreRecalculationEvent;
import com.mindshift.model.TrackerEntry;
import com.mindshift.model.User;
import com.mindshift.repository.TrackerEntryRepository;
import com.mindshift.service.TrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrackerServiceImpl implements TrackerService {

    private final TrackerEntryRepository trackerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public TrackerResponse save(User user, TrackerRequest request) {
        TrackerEntry entry = trackerRepository.findByUserAndDayKey(user, request.getDayKey())
                .orElseGet(() -> TrackerEntry.builder().user(user).dayKey(request.getDayKey()).build());

        entry.setAwareness(request.getAwareness());
        entry.setControl(request.getControl());
        entry.setPresence(request.getPresence());
        entry.setSleep(request.getSleep());
        entry.setConfidence(request.getConfidence());
        entry.setPeace(request.getPeace());

        trackerRepository.save(entry);
        eventPublisher.publishEvent(new ScoreRecalculationEvent(user.getId()));
        return TrackerResponse.from(entry);
    }

    @Override
    public Map<String, TrackerResponse> getAll(User user) {
        Map<String, TrackerResponse> result = new LinkedHashMap<>();
        trackerRepository.findByUser(user).forEach(t -> result.put(t.getDayKey(), TrackerResponse.from(t)));
        return result;
    }
}
