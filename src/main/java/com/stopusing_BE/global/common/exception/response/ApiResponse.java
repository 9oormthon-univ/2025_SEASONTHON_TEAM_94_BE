package com.stopusing_BE.global.common.exception.response;

import com.stopusing_BE.global.common.exception.BaseErrorCode;
import com.stopusing_BE.global.common.exception.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
  private final boolean success;
  private final int status;
  private final String code;
  private final String message;
  private final T data;

  @Builder
  public ApiResponse(boolean success, int status, String code, String message, T data) {
    this.success = success;
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  // ✅ 성공 응답 (SuccessCode 기반)
  public static <T> ApiResponse<T> success( T data) {
    return ApiResponse.<T>builder()
        .success(true)
        .status(SuccessCode.OK.getHttpStatus().value())
        .code(SuccessCode.OK.getCode())
        .message(SuccessCode.OK.getMessage())
        .data(data)
        .build();
  }

  // ✅ 실패 응답 (ErrorCode 기반)
  public static <T> ApiResponse<T> error(BaseErrorCode errorCode, String customMessage) {
    return ApiResponse.<T>builder()
        .success(false)
        .status(errorCode.getHttpStatus().value())
        .code(errorCode.getCode())
        .message(customMessage != null ? customMessage : errorCode.getMessage())
        .data(null)
        .build();
  }

  public static <T> ApiResponse<T> error(BaseErrorCode errorCode) {
    return error(errorCode, null);
  }
}