package com.stopusing_BE.domain.transaction.dto.response;

import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import java.time.LocalDateTime;
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
  private String bankName;
  private String memo;
  private TransactionType type;
  private String userUid;
  private TransactionCategory category;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime startedAt;

  // 엔티티 -> DTO 변환 메서드
  public static TransactionResponse fromEntity(Transaction transaction) {
    return TransactionResponse.builder()
        .id(transaction.getId())
        .price(transaction.getPrice())
        .title(transaction.getTitle())
        .bankName(transaction.getBankName())
        .memo(transaction.getMemo())
        .type(transaction.getType())
        .category(transaction.getCategory())
        .userUid(transaction.getUser().getUid())
        .createdAt(transaction.getCreatedAt())
        .updatedAt(transaction.getUpdatedAt())
        .startedAt(transaction.getStartedAt())
        .build();
  }
}