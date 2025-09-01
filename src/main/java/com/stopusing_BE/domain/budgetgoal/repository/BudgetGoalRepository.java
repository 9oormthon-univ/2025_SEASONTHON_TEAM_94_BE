package com.stopusing_BE.domain.budgetgoal.repository;

import com.stopusing_BE.domain.budgetgoal.entity.BudgetGoal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetGoalRepository extends JpaRepository<BudgetGoal, Long> {
  // userUid + createdAt이 [start, end) 구간인 것 중 가장 최근 1개
  Optional<BudgetGoal> findFirstByUser_UidAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
      String userUid, LocalDateTime startInclusive, LocalDateTime endExclusive
  );
}
