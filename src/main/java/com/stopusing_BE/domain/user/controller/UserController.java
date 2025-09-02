package com.stopusing_BE.domain.user.controller;

import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.user.dto.request.UserUpdateRequest;
import com.stopusing_BE.domain.user.dto.response.UserResponse;
import com.stopusing_BE.domain.user.service.UserService;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PutMapping()
  public ApiResponse<UserResponse> update(
      @RequestBody UserUpdateRequest request,
      @RequestParam() String userUid
  ) {
    return ApiResponse.success(userService.update(userUid, request));
  }

  @GetMapping("/me")
  public ApiResponse<UserResponse> getMe(
      @RequestParam() String userUid
  ) {
    return ApiResponse.success(userService.getMe(userUid));
  }

}
