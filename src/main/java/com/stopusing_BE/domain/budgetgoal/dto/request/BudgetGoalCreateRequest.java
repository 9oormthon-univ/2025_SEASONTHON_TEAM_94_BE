package com.stopusing_BE.domain.budgetgoal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BudgetGoalCreateRequest {
  @NotNull(message = "가격을 입력해주세요")
  private Long price;

  @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
  private String userUid;

}
