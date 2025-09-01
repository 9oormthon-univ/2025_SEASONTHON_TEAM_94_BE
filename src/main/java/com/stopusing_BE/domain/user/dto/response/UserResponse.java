package com.stopusing_BE.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
  private String id;
  private String role;
  private String username;
  private String nickname;

}
