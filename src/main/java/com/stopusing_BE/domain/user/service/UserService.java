package com.stopusing_BE.domain.user.service;

import com.stopusing_BE.domain.user.dto.request.UserUpdateRequest;
import com.stopusing_BE.domain.user.dto.response.UserResponse;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.repository.UserRepository;
import com.stopusing_BE.global.common.exception.CustomException;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public User getByIdOrThrow(String userUid) {
    return userRepository.findByUid(userUid)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public UserResponse update(String userUid, UserUpdateRequest request) {
    User user = getByIdOrThrow(userUid);
    if(!user.getUid().equals(userUid)) {
      throw new CustomException(ErrorCode.FORBIDDEN, "본인만 변경할 수 있습니다.");
    }

    if(request.getNickname() != null) {
      user.setNickname(request.getNickname());
    }

    return UserResponse.fromEntity(user);

  }

  @Transactional
  public UserResponse getMe(String userUid) {
    User user = getByIdOrThrow(userUid);
    return UserResponse.fromEntity(user);
  }

  @Transactional
  public void setRegistered(String userUid) {
    User user = getByIdOrThrow(userUid);
    user.setIsRegistered(true);
  }
}