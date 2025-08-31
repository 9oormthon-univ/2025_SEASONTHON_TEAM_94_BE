package com.stopusing_BE.domain.category.exception;

import com.stopusing_BE.global.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements BaseErrorCode {
  // 404 Error
  CATEGORY_ERROR_CODE(HttpStatus.NOT_FOUND, "404", "Some categories not found.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

}
