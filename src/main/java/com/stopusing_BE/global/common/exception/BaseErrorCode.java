package com.stopusing_BE.global.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
  HttpStatus getHttpStatus();
  String getCode();
  String getMessage();
}
