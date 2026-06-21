package com.mindshift.repository;

import com.mindshift.model.DailyProgress;
import com.mindshift.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {
    Optional<DailyProgress> findByUser(User user);
    Optional<DailyProgress> findByUserId(Long userId);
}
