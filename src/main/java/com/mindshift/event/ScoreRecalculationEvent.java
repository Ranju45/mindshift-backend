package com.mindshift.event;

import org.springframework.context.ApplicationEvent;

/**
 * Observer pattern via Spring's ApplicationEventPublisher. Any service
 * that changes data feeding the God Mode score (journal, tracker,
 * progress) publishes this event instead of recalculating the score
 * itself — keeping that single responsibility in ScoreService only.
 */
public class ScoreRecalculationEvent extends ApplicationEvent {
    private final Long userId;

    public ScoreRecalculationEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }

    public Long getUserId() { return userId; }
}
