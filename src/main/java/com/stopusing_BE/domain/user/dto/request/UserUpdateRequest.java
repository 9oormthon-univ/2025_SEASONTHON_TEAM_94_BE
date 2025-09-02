package com.stopusing_BE.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserUpdateRequest {
  @NotNull(message = "변경 할 닉네임을 입력해야합니다.")
  private String nickname;
}
