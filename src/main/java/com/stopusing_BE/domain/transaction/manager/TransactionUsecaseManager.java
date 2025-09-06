package com.stopusing_BE.domain.transaction.manager;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateByAlertRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionTypeUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCalendarResponse;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  public List<TransactionResponse> getAllByTypeAndRange(
      String userUid, TransactionType type, LocalDate startAt, LocalDate endAt) {

    // 공통 타임존: 한국 표준시
    final ZoneId seoulZone = ZoneId.of("Asia/Seoul");

    // 사용자 조회
    User user = userService.getByIdOrThrow(userUid);

    // 무료 6개월 제한 (KST 기준 날짜)
    if (!user.getIsPremium()) {
      LocalDate sixMonthsAgo = LocalDate.now(seoulZone).minusMonths(6);

      // startAt만 있는 경우 → startAt이 제한 이전이면 forbidden
      if (startAt != null && endAt == null && startAt.isBefore(sixMonthsAgo)) {
        throw new CustomException(ErrorCode.FORBIDDEN_PREMIUM, "무료 사용자는 6개월 이전 데이터에 접근할 수 없습니다.");
      }

      // endAt만 있는 경우 → endAt이 제한 이전이면 forbidden
      if (startAt == null && endAt != null && endAt.isBefore(sixMonthsAgo)) {
        throw new CustomException(ErrorCode.FORBIDDEN_PREMIUM, "무료 사용자는 6개월 이전 데이터에 접근할 수 없습니다.");
      }

      // 둘 다 있는 경우 → 조회 구간에 제한 이전 날짜가 포함되면 forbidden
      if (startAt != null && endAt != null && startAt.isBefore(sixMonthsAgo)) {
        throw new CustomException(ErrorCode.FORBIDDEN_PREMIUM, "무료 사용자는 6개월 이전 데이터에 접근할 수 없습니다.");
      }
    }

    // 1) 둘 다 null → 타입만 조회
    if (startAt == null && endAt == null) {
      return transactionService.getAllByType(userUid, type)
          .stream()
          .map(TransactionResponse::fromEntity)
          .collect(Collectors.toList());
    }

    // 2) startAt 있음 → [startAt(00:00 KST), now(KST))  (끝 exclusive)
    if (startAt != null && endAt == null) {
      LocalDateTime start = startAt.atStartOfDay(seoulZone).toLocalDateTime();  // inclusive
      LocalDateTime endEx = ZonedDateTime.now(seoulZone).toLocalDateTime();     // exclusive (현재 시각)

      // 같은 날(== 오늘 00:00)도 허용, 미래만 금지
      if (start.isAfter(endEx)) {
        throw new CustomException(ErrorCode.INVALID_REQUEST, "startAt가 현재 일자를 넘었습니다.");
      }

      return transactionService.getByTypeAndDateRange(userUid, type, start, endEx)
          .stream()
          .map(TransactionResponse::fromEntity)
          .collect(Collectors.toList());
    }

    // 3) endAt만 있음 → (-∞, endAt+1일 00:00 KST)  (끝 exclusive)
    if (startAt == null) {
      LocalDateTime endEx = endAt.plusDays(1).atStartOfDay(seoulZone).toLocalDateTime(); // exclusive
      return transactionService.getByTypeAndStartAtLessThan(userUid, type, endEx)
          .stream()
          .map(TransactionResponse::fromEntity)
          .collect(Collectors.toList());
    }

    // 4) 둘 다 있음 → [startAt(00:00 KST), endAt+1일 00:00 KST)  (끝 exclusive)
    LocalDateTime start = startAt.atStartOfDay(seoulZone).toLocalDateTime();           // inclusive
    LocalDateTime endEx = endAt.plusDays(1).atStartOfDay(seoulZone).toLocalDateTime(); // exclusive

    // 시작이 현재(KST)보다 미래이면 금지
    if (start.isAfter(ZonedDateTime.now(seoulZone).toLocalDateTime())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST, "StartAt 가 현재 일자를 넘었습니다.");
    }

    return transactionService.getByTypeAndDateRange(userUid, type, start, endEx)
        .stream()
        .map(TransactionResponse::fromEntity)
        .collect(Collectors.toList());
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

  @Transactional(readOnly = true)
  public TransactionCalendarResponse getTotalPricesByType(
      String userUid, TransactionType type, LocalDate date) {

    // 사용자 조회
    User user = userService.getByIdOrThrow(userUid);

    // 무료 사용자라면 6개월 이전 조회 차단
    if (!user.getIsPremium()) {
      LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
      if (date.isBefore(sixMonthsAgo)) {
        throw new CustomException(ErrorCode.FORBIDDEN_PREMIUM, "무료 사용자는 6개월 이전 데이터에 접근할 수 없습니다.");
      }
    }

    int year = date.getYear();
    int month = date.getMonthValue();

    LocalDate firstDay = LocalDate.of(year, month, 1);
    LocalDateTime start = firstDay.atStartOfDay();                 // yyyy-MM-01 00:00
    LocalDateTime end = firstDay.plusMonths(1).atStartOfDay();     // 다음달 01 00:00  (주의: plusDays가 아님!)

    int daysInMonth = firstDay.lengthOfMonth();
    Map<Long, Long> result = new LinkedHashMap<>();
    for (int d = 1; d <= daysInMonth; d++) result.put((long) d, 0L);

    // [start, end) 범위로 이미 한 달치만 조회된다고 가정
    List<Transaction> transactions =
        transactionService.getByTypeAndDateRange(userUid, type, start, end);

    for (Transaction tx : transactions) {
      LocalDate startDate = tx.getStartedAt().toLocalDate();
      long day = startDate.getDayOfMonth();
      result.merge(day, tx.getPrice(), Long::sum);
    }

    return TransactionCalendarResponse.builder()
        .totals(result)
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

