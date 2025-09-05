package com.stopusing_BE.domain.budgetgoal.manager;

import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalCreateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalUpdateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.response.BudgetGoalResponse;
import com.stopusing_BE.domain.budgetgoal.entity.BudgetGoal;
import com.stopusing_BE.domain.budgetgoal.service.BudgetGoalService;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.service.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BudgetGoalManager {
  private final BudgetGoalService budgetGoalService;
  private final UserService userService;

    /** 생성 */
    @Transactional
    public BudgetGoalResponse create(String userUid ,BudgetGoalCreateRequest request) {
      User user = userService.getByIdOrThrow(userUid);
      BudgetGoal budgetGoal = budgetGoalService.create(user, request);
      
      // 목표 지출 생성 시 사용자를 가입 완료 상태로 변경
      userService.setRegistered(userUid);
      
      return BudgetGoalResponse.fromEntity(budgetGoal);
    }

    /** 단건 조회 (소유권 검증 포함) */
    @Transactional(readOnly = true)
    public BudgetGoalResponse getById(String userUid, Long id) {
      userService.getByIdOrThrow(userUid);
      BudgetGoal budgetGoal = budgetGoalService.getByIdOrThrow(id);
      return BudgetGoalResponse.fromEntity(budgetGoal);
    }

    /** 특정 날짜가 속한 월의 최신 BudgetGoal */
    @Transactional(readOnly = true)
    public BudgetGoalResponse getByDate(String userUid, LocalDate date) {
      userService.getByIdOrThrow(userUid);
      // date가 null이면 오늘 날짜로 설정
      if (date == null) {
        date = LocalDate.now();
      }

      BudgetGoal goal = budgetGoalService.getLatestOfMonth(userUid, date);
      return BudgetGoalResponse.fromEntity(goal);
    }

    /** 수정 */
    @Transactional
    public BudgetGoalResponse update(BudgetGoalUpdateRequest request,String userUid, Long id) {
      userService.getByIdOrThrow(userUid);
      BudgetGoal budgetGoal = budgetGoalService.update(request, userUid, id);
      return BudgetGoalResponse.fromEntity(budgetGoal);
    }
}
