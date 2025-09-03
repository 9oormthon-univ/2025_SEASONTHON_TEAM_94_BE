package com.stopusing_BE.domain.user.dto.response;

import com.stopusing_BE.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
  private String id;
  private String role;
  private String username;
  private String nickname;
  private String email;

  public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
        .id(user.getUid())
        .role(user.getRole())
        .username(user.getUsername())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .build();
  }

}
