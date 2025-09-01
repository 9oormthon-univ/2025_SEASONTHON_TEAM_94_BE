package com.stopusing_BE.domain.user.service;

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
}