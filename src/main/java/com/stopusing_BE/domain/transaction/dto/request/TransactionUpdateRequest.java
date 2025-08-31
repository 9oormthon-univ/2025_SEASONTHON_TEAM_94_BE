package com.stopusing_BE.domain.transaction.dto.request;

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

  // 선택: 기존 카테고리들을 연결하려면 ID 목록 전달 (없으면 null/빈 리스트)
  private List<Long> categoryIds;
}
