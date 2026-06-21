package com.mindshift.model;

import com.mindshift.model.enums.ThoughtCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "journal_entries", indexes = @Index(name = "idx_journal_user", columnList = "user_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private Integer intensity; // 0-10

    @ElementCollection
    @CollectionTable(name = "journal_tags", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = List.of();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ThoughtCategory category = ThoughtCategory.UNCATEGORIZED;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }
}
