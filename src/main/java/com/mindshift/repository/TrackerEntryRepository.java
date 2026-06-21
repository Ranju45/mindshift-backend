package com.mindshift.repository;

import com.mindshift.model.TrackerEntry;
import com.mindshift.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackerEntryRepository extends JpaRepository<TrackerEntry, Long> {
    List<TrackerEntry> findByUser(User user);
    Optional<TrackerEntry> findByUserAndDayKey(User user, String dayKey);
}
