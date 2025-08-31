package com.stopusing_BE.domain.transaction.service;


import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.exception.TransactionErrorCode;
import com.stopusing_BE.domain.transaction.repository.TransactionRepository;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.global.common.exception.CustomException;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;

  @Transactional
  public Transaction create(User user,
      List<Category> categories,
      TransactionCreateRequest request) {

    Transaction transaction = Transaction.builder()
        .type(TransactionType.NONE)
        .startedAt(request.getStartAt() != null ? request.getStartAt() : LocalDateTime.now())
        .price(request.getPrice())
        .title(request.getTitle())
        .user(user)
        .build();

    if (categories != null) categories.forEach(transaction::addCategory);
    return transactionRepository.save(transaction);
  }

  @Transactional(readOnly = true)
  public Transaction getByIdOrThrow(Long id) {
    return transactionRepository.findById(id)
        .orElseThrow(() -> new CustomException(TransactionErrorCode.TRANSACTION_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<Transaction> getAllByType(Long userId,TransactionType type) {
    return transactionRepository.findAllByUser_IdAndType(userId,type);
  }

  @Transactional
  public Transaction update(TransactionUpdateRequest request,Long userId,Long id,
      List<Category> newCategories) {
    Transaction tx = getByIdOrThrow(id);
    // 소유자 검사
    if (!Objects.equals(tx.getUser().getId(), userId)) {
      throw new CustomException(ErrorCode.FORBIDDEN, "본인의 거래만 변경할 수 있습니다.");
    }
    if (request.getPrice() != null)     tx.setPrice(request.getPrice());
    if (request.getTitle() != null)     tx.setTitle(request.getTitle());
    if (request.getType() != null)      tx.setType(request.getType());
    if (request.getStartAt() != null) tx.setStartedAt(request.getStartAt());
    if (request.getCategoryIds() != null) {      // null: 유지, 빈 리스트: 모두 해제
      tx.replaceCategories(newCategories);   // 조인만 갈아끼움, Category는 삭제 안됨
    }

    return tx; // 변경감지
  }

  @Transactional
  public Transaction deleteById(Long id, Long userId) {
    Transaction tx = getByIdOrThrow(id);

    // 소유자 검사
    if (!Objects.equals(tx.getUser().getId(), userId)) {
      throw new CustomException(ErrorCode.FORBIDDEN, "본인의 거래만 삭제할 수 있습니다.");
    }

    transactionRepository.delete(tx); // 영속 엔티티 삭제
    return tx;
  }





}