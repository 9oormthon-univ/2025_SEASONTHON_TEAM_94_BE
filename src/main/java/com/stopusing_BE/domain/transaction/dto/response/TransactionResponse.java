package com.stopusing_BE.domain.transaction.dto.response;

import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionResponse {

  private Long id;
  private Long price;
  private String title;
  private TransactionType type;
  private Long userId;
  private List<Long> categoryIds;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime startAt;

  // 엔티티 -> DTO 변환 메서드
  public static TransactionResponse fromEntity(Transaction transaction) {
    return TransactionResponse.builder()
        .id(transaction.getId())
        .price(transaction.getPrice())
        .title(transaction.getTitle())
        .type(transaction.getType())
        .userId(transaction.getUser().getId())
        .categoryIds(transaction.getCategories()
            .stream()
            .map(Category::getId)
            .collect(Collectors.toList()))
        .createdAt(transaction.getCreatedAt())
        .updatedAt(transaction.getUpdatedAt())
        .startAt(transaction.getStartedAt())
        .build();
  }
}