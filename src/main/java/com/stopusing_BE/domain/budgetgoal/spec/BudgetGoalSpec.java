package com.stopusing_BE.domain.budgetgoal.spec;

import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalCreateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.request.BudgetGoalUpdateRequest;
import com.stopusing_BE.domain.budgetgoal.dto.response.BudgetGoalResponse;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface BudgetGoalSpec {

  @Operation(summary = "목표 초과지출 내역 생성", description = "목표 초과지출 내역 생성합니다.")
  ApiResponse<BudgetGoalResponse> create(
      @RequestBody BudgetGoalCreateRequest request
  );

  @Operation(summary = "목표 초과지출 내역 상세 정보 받기", description = "목표 초과지출 내역 상세 정보 받습니다.")
  ApiResponse<BudgetGoalResponse> getById(
      @RequestParam String userUid,
      @PathVariable Long id
  );

  @Operation(summary = "목표 초과지출 내역 날짜 월 최근 정보 받기", description = "목표 초과지출 내역 날짜를 받으면 그 날짜의 월에서 가장 최근 데이터를 받습니다.")
  ApiResponse<BudgetGoalResponse> getByDate(
      @RequestParam String userUid,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date
  );

  @Operation(summary = "목표 초과지출 내역 수정", description = "목표 초과지출 내역 수정합니다.")
  ApiResponse<BudgetGoalResponse> update(
      @RequestBody BudgetGoalUpdateRequest request,
      @RequestParam() String userUid,
      @PathVariable Long id
  );


}
