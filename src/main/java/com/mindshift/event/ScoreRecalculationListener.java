package com.mindshift.event;

import com.mindshift.model.DailyProgress;
import com.mindshift.repository.DailyProgressRepository;
import com.mindshift.repository.JournalEntryRepository;
import com.mindshift.repository.TrackerEntryRepository;
import com.mindshift.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Subscriber side of the Observer pattern. Runs asynchronously so the
 * triggering HTTP request (e.g. "save journal entry") doesn't wait on
 * score math.
 *
 * Score formula mirrors the frontend's local calculation:
 *   control% * 0.4 + streakDays * 12 + journalEntries * 3 + completedDays * 8
 */
@Component
@RequiredArgsConstructor
public class ScoreRecalculationListener {

    private final DailyProgressRepository progressRepository;
    private final JournalEntryRepository journalRepository;
    private final TrackerEntryRepository trackerRepository;
    private final UserRepository userRepository;

    @Async
    @EventListener
    public void onScoreRecalculation(ScoreRecalculationEvent event) {
        userRepository.findById(event.getUserId()).ifPresent(user -> {
            DailyProgress progress = progressRepository.findByUser(user).orElse(null);
            if (progress == null) return;

            int streak = trackerRepository.findByUser(user).size();
            int entries = journalRepository.findByUserOrderByCreatedAtDesc(user).size();
            int completed = progress.getCompletedDays().size();

            int controlPct = trackerRepository.findByUser(user).stream()
                    .mapToInt(t -> (t.getAwareness() + t.getControl() + t.getPresence()
                            + t.getSleep() + t.getConfidence() + t.getPeace()) * 100 / 60)
                    .sum() / Math.max(1, streak);

            int score = (int) Math.round(controlPct * 0.4 + streak * 12 + entries * 3 + completed * 8);
            progress.setGodModeScore(score);
            progressRepository.save(progress);
        });
    }
}
