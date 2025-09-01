package com.stopusing_BE.domain.transaction.repository;

import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findAllByUser_UidAndType(String userUid, TransactionType type);
}
