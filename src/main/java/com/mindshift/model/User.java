package com.mindshift.model;

import com.mindshift.model.enums.Goal;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

/**
 * User aggregate root. Implements UserDetails directly to avoid an
 * extra adapter class — acceptable for a single-role system like this.
 */
@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_email", columnList = "email", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hash

    @Column(nullable = false)
    @Builder.Default
    private Integer severity = 9;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Goal goal = Goal.SUPERHUMAN;

   @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_impacts", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "impact")
    @Builder.Default
    private List<String> impacts = List.of();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }

    // ── UserDetails contract ──
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
