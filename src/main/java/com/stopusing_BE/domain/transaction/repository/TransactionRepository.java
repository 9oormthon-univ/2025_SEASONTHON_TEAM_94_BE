package com.stopusing_BE.domain.transaction.repository;

import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  // 타입만 (정렬: startedAt 내림차순)
  List<Transaction> findByUser_UidAndTypeOrderByStartedAtDesc(
      String userUid, TransactionType type
  );

  // [startInclusive, endExclusive)
  List<Transaction> findByUser_UidAndTypeAndStartedAtGreaterThanEqualAndStartedAtLessThanOrderByStartedAtDesc(
      String userUid, TransactionType type,
      LocalDateTime startInclusive, LocalDateTime endExclusive
  );

  // (-∞, endExclusive)
  List<Transaction> findByUser_UidAndTypeAndStartedAtLessThanOrderByStartedAtDesc(
      String userUid, TransactionType type, LocalDateTime endExclusive
  );

}
