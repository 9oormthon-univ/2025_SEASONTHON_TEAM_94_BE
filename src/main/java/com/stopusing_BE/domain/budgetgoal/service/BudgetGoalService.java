package com.stopusing_BE.domain.budgetgoal.service;

import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalCreateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalUpdateRequest;
import com.stopusing_BE.domain.budgetgoal.entity.BudgetGoal;
import com.stopusing_BE.domain.budgetgoal.repository.BudgetGoalRepository;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.global.common.exception.CustomException;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetGoalService {

  private final BudgetGoalRepository budgetGoalRepository;

  @Transactional
  public BudgetGoal create(User user, BudgetGoalCreateRequest request) {
    BudgetGoal budgetGoal = BudgetGoal.builder()
        .price(request.getPrice())
        .user(user)
        .build();

    return budgetGoalRepository.save(budgetGoal);
  }

  @Transactional
  public BudgetGoal getByIdOrThrow(Long id) {
    return budgetGoalRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Id에 맞는 지출 내역을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public BudgetGoal getLatestOfMonth(String userUid, LocalDate anyDateInMonth) {
    LocalDate monthStart = anyDateInMonth.withDayOfMonth(1);
    LocalDateTime start = monthStart.atStartOfDay();           // inclusive
    LocalDateTime end = monthStart.plusMonths(1).atStartOfDay(); // exclusive

    return budgetGoalRepository
        .findFirstByUser_UidAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
            userUid, start, end
        )
        .orElseThrow(() ->
            new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 월의 BudgetGoal이 없습니다."));
  }

  @Transactional
  public BudgetGoal update(BudgetGoalUpdateRequest request, String userUid, Long id) {
    BudgetGoal budgetGoal = getByIdOrThrow(id);

    if(!Objects.equals(budgetGoal.getUser().getUid(), userUid)) {
      throw new CustomException(ErrorCode.FORBIDDEN, "본인의 내역만 변경할 수 있습니다.");
    }

    if(request.getPrice() != null) budgetGoal.setPrice(request.getPrice());

    return budgetGoal;
  }


}
