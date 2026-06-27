package com.mindshift.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

/**
 * One row per user. Tracks the 7-day protocol cursor.
 * completedDays is intentionally small (max 7 ints) so an ElementCollection
 * is fine — no need for a join entity with its own lifecycle.
 */
@Entity
@Table(name = "daily_progress")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentDay = 1;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "completed_days", joinColumns = @JoinColumn(name = "progress_id"))
    @Column(name = "day_number")
    @Builder.Default
    private List<Integer> completedDays = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Integer godModeScore = 0;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() { this.startedAt = Instant.now(); this.updatedAt = Instant.now(); }

    @PreUpdate
    void preUpdate() { this.updatedAt = Instant.now(); }
}
