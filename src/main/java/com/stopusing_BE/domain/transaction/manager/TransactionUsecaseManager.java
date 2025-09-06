package com.stopusing_BE.domain.transaction.manager;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateByAlertRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionTypeUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionReportResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.service.TransactionCategoryService;
import com.stopusing_BE.domain.transaction.service.TransactionService;
import com.stopusing_BE.domain.transaction.util.CategoryEstimator;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.service.UserService;
import com.stopusing_BE.global.common.exception.CustomException;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionUsecaseManager {

  private final UserService userService;
  private final TransactionService transactionService;
  private final TransactionCategoryService transactionCategoryService;
  private final CategoryEstimator categoryEstimator;

  @Transactional
  public TransactionResponse createByAlert(TransactionCreateByAlertRequest request) {
    User user = userService.getByIdOrThrow(request.getUserUid());
    // AI를 통한 카테고리 추정
    TransactionCategory estimatedCategory = categoryEstimator.estimateCategory(request.getTitle());
    Transaction transaction = transactionService.createByAlert(user, request, estimatedCategory);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public TransactionResponse create(String userUid,TransactionCreateRequest request) {
    User user = userService.getByIdOrThrow(userUid);
    Transaction transaction = transactionService.create(user, request);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public TransactionResponse getById(String userUid,Long id) {
    userService.getByIdOrThrow(userUid);
    Transaction transaction = transactionService.getByIdOrThrow(id);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional(readOnly = true)
  public List<TransactionResponse> getAllByTypeAndRange(String userUid, TransactionType type,
      LocalDate startAt, LocalDate endAt) {

    // 1) 둘 다 null → 타입만
    if (startAt == null && endAt == null) {
      return transactionService.getAllByType(userUid, type).stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());
    }

    // 2) startAt 있음 → [startAt, now)
    if (startAt != null && endAt == null) {

      LocalDateTime start = startAt.atStartOfDay();      // inclusive
      LocalDateTime endEx = LocalDateTime.now();           // exclusive

      if (start.isAfter(endEx)) {
        throw new CustomException(ErrorCode.INVALID_REQUEST, "startAt가 현재보다 빠릅니다.");
      }
      return transactionService.getByTypeAndDateRange(userUid, type, start, endEx).stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());

    }

    // 3) endDate만 있음 → (-∞, endDate+1일 00:00)
    if (startAt == null) {
      LocalDateTime endEx = endAt.plusDays(1).atStartOfDay(); // exclusive
      return transactionService.getByTypeAndStartAtLessThan(userUid, type, endEx).stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());
    }

    // 4) 둘 다 있음 → [startAt, endDate] (끝은 exclusive로 변환)
    LocalDateTime start = startAt.atStartOfDay();                 // inclusive
    LocalDateTime endEx = endAt.plusDays(1).atStartOfDay();       // exclusive

    if(start.isAfter(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST, "StartAt 가 현재 일자를 넘었습니다.");
    }




    return transactionService.getByTypeAndDateRange(userUid, type, start, endEx).stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());

  }

  public TransactionReportResponse buildReport(List<TransactionResponse> items) {
    if (items == null || items.isEmpty()) {
      return TransactionReportResponse.builder()
          .totalPrice(0L)
          .totalCount(0L)
          .startAt(null)
          .endAt(null)
          .build();
    }

    long totalPrice = items.stream()
        .map(TransactionResponse::getPrice)
        .filter(Objects::nonNull)
        .mapToLong(Long::longValue)
        .sum();

    long totalCount = items.size();

    LocalDateTime startAt = items.stream()
        .map(TransactionResponse::getStartedAt)
        .filter(Objects::nonNull)
        .min(LocalDateTime::compareTo)
        .orElse(null);

    LocalDateTime endAt = items.stream()
        .map(TransactionResponse::getStartedAt)
        .filter(Objects::nonNull)
        .max(LocalDateTime::compareTo)
        .orElse(null);

    return TransactionReportResponse.builder()
        .totalPrice(totalPrice)
        .totalCount(totalCount)
        .startAt(startAt)
        .endAt(endAt)
        .build();
  }

  @Transactional
  public TransactionResponse update(TransactionUpdateRequest request,String userUid,Long id) {
    userService.getByIdOrThrow(userUid);
    Transaction tx = transactionService.update(request, userUid,id);
    return TransactionResponse.fromEntity(tx);
  }

  @Transactional
  public TransactionResponse delete(String userUid,Long id) {
    Transaction transaction = transactionService.deleteById(id, userUid);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional(readOnly = true)
  public List<TransactionCategoryResponse> getAllCategories() {
    return transactionCategoryService.getAll();
  }

  @Transactional
  public TransactionResponse updateTypeByAlert(TransactionTypeUpdateRequest request) {
    User user = userService.getByIdOrThrow(request.getUserUid());
    Transaction transaction = transactionService.updateTypeByAlert(request);
    return TransactionResponse.fromEntity(transaction);
  }

}

