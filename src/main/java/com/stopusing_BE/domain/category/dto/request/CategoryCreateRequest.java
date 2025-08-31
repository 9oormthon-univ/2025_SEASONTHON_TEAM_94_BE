package com.stopusing_BE.domain.category.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryCreateRequest {
  @NotNull(message = "사용자 ID는 필수 입력 값입니다.")
  private String name;
}
