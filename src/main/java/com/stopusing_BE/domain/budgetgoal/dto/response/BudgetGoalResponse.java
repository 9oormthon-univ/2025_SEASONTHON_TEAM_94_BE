package com.stopusing_BE.domain.budgetgoal.dto.response;

import com.stopusing_BE.domain.budgetgoal.entity.BudgetGoal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BudgetGoalResponse {
  private Long id;
  private Long price;
  private String userUid;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static BudgetGoalResponse fromEntity(BudgetGoal budgetGoal) {
    return BudgetGoalResponse.builder()
        .id(budgetGoal.getId())
        .price(budgetGoal.getPrice())
        .userUid(budgetGoal.getUser().getUid())
        .createdAt(budgetGoal.getCreatedAt())
        .updatedAt(budgetGoal.getUpdatedAt())
        .build();
  }
}
