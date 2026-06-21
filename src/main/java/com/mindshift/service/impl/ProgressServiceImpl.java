package com.mindshift.service.impl;

import com.mindshift.dto.response.ProgressResponse;
import com.mindshift.event.ScoreRecalculationEvent;
import com.mindshift.exception.BadRequestException;
import com.mindshift.exception.ResourceNotFoundException;
import com.mindshift.model.DailyProgress;
import com.mindshift.model.User;
import com.mindshift.repository.DailyProgressRepository;
import com.mindshift.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private static final int MAX_DAY = 7;

    private final DailyProgressRepository progressRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ProgressResponse get(User user) {
        return ProgressResponse.from(findOrThrow(user));
    }

    @Override
    @Transactional
    public ProgressResponse completeDay(User user, int day) {
        if (day < 1 || day > MAX_DAY) {
            throw new BadRequestException("Day must be between 1 and " + MAX_DAY);
        }
        DailyProgress progress = findOrThrow(user);

        // LinkedHashSet preserves insertion order while de-duplicating —
        // avoids the caller accidentally double-completing the same day.
        var completed = new LinkedHashSet<>(progress.getCompletedDays());
        completed.add(day);
        progress.setCompletedDays(new ArrayList<>(completed));
        progress.setCurrentDay(Math.max(progress.getCurrentDay(), Math.min(day + 1, MAX_DAY)));

        progressRepository.save(progress);
        eventPublisher.publishEvent(new ScoreRecalculationEvent(user.getId()));
        return ProgressResponse.from(progress);
    }

    private DailyProgress findOrThrow(User user) {
        return progressRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Progress record not found for user"));
    }
}
