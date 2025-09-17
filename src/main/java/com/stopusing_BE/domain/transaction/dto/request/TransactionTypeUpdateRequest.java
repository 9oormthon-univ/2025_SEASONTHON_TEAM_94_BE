package com.stopusing_BE.domain.transaction.dto.request;

import com.stopusing_BE.domain.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionTypeUpdateRequest {
  
  @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
  private String userUid;

  private Long id;
  
  @NotNull(message = "Transaction Type은 필수 입력 값입니다.")
  private TransactionType type;
}