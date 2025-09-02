package com.stopusing_BE.domain.transaction.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionCreateByAlertRequest {
  @NotNull(message = "가격은 필수 입력 값입니다.")
  private Long price;

  private LocalDateTime startAt;

  @NotBlank(message = "거래 제목은 필수 입력 값입니다.")
  private String title;

  @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
  private String userUid;

}
