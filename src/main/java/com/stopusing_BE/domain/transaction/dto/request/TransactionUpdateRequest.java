package com.stopusing_BE.domain.transaction.dto.request;

import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionUpdateRequest {
  @NotNull(message = "가격은 필수 입력 값입니다.")
  @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
  private Long price;

  private TransactionType type;

  private LocalDateTime startAt;

  @NotBlank(message = "거래 제목은 필수 입력 값입니다.")
  private String title;

  @NotBlank(message = "은행명을 입력하세요")
  private String bankName;

  @NotBlank(message = "더치페이 명수를 입력해주세요")
  private Integer splitCount;

  private String memo;

  private TransactionCategory category;

}
