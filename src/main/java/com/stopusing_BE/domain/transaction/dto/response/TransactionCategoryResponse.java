package com.stopusing_BE.domain.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryResponse {
  private String value; // enum name (e.g., "FOOD")
  private String label; // 한글 라벨 (e.g., "식비")
}
