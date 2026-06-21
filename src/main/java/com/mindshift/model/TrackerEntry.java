package com.mindshift.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * One row per (user, dayKey). The six dimensions map 1:1 to the
 * frontend's TRACK_METRICS array.
 */
@Entity
@Table(name = "tracker_entries",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "day_key"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrackerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "day_key", nullable = false, length = 16)
    private String dayKey; // e.g. "day_1".."day_7"

    @Column(nullable = false) private Integer awareness;
    @Column(nullable = false) private Integer control;
    @Column(nullable = false) private Integer presence;
    @Column(nullable = false) private Integer sleep;
    @Column(nullable = false) private Integer confidence;
    @Column(nullable = false) private Integer peace;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}
