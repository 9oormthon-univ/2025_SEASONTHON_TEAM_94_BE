package com.stopusing_BE.domain.budgetgoal.controller;

import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalCreateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalUpdateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.response.BudgetGoalResponse;
import com.stopusing_BE.domain.budgetgoal.manager.BudgetGoalManager;
import com.stopusing_BE.domain.budgetgoal.spec.BudgetGoalSpec;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budgetgoals")
@RequiredArgsConstructor
public class BudgetGoalController implements BudgetGoalSpec {

  private final BudgetGoalManager budgetGoalManager;

  @Override
  @PostMapping
  public ApiResponse<BudgetGoalResponse> create(CustomOAuth2UserResponse currentUser,BudgetGoalCreateRequest request) {
    String userUid = currentUser.getUserUid();
    return ApiResponse.success(budgetGoalManager.create(userUid,request));
  }

  @Override
  @GetMapping("/{id}")
  public ApiResponse<BudgetGoalResponse> getById(CustomOAuth2UserResponse currentUser, Long id) {
    String userUid = currentUser.getUserUid();
    return ApiResponse.success(budgetGoalManager.getById(userUid, id));
  }

  @Override
  @GetMapping
  public ApiResponse<BudgetGoalResponse> getByDate(CustomOAuth2UserResponse currentUser, LocalDate date) {
    String userUid = currentUser.getUserUid();
    return ApiResponse.success(budgetGoalManager.getByDate(userUid, date));
  }

  @Override
  @PutMapping("/{id}")
  public ApiResponse<BudgetGoalResponse> update(BudgetGoalUpdateRequest request, CustomOAuth2UserResponse currentUser,
      Long id) {
    String userUid = currentUser.getUserUid();
    return ApiResponse.success(budgetGoalManager.update(request, userUid, id));
  }
}
