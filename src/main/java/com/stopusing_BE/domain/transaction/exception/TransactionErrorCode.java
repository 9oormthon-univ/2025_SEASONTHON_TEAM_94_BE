package com.stopusing_BE.domain.transaction.exception;

import com.stopusing_BE.global.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TransactionErrorCode implements BaseErrorCode {
  TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 Id에 맞는 지출 내역을 찾을 수 없습니다.");
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
