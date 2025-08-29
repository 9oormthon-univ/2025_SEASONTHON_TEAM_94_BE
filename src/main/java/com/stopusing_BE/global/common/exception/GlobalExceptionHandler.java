package com.stopusing_BE.global.common.exception;




//import io.swagger.v3.oas.annotations.Hidden;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  // ✅ DTO 유효성 검증 실패 (@Valid @RequestBody)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String errorMsg = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
        .collect(Collectors.joining(", "));
    return buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorMsg);
  }

  // ✅ @Validated + @RequestParam 실패
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
    String errorMsg = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
        .collect(Collectors.joining(", "));
    return buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorMsg);
  }

  // ✅ 파라미터 타입 불일치
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String errorMsg = String.format("'%s' 파라미터는 '%s' 타입이어야 합니다.",
        ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "알 수 없음");
    return buildErrorResponse(ErrorCode.TYPE_MISMATCH, errorMsg);
  }

  // ✅ 필수 파라미터 누락
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
    String errorMsg = String.format("필수 파라미터 '%s'가 누락되었습니다.", ex.getParameterName());
    return buildErrorResponse(ErrorCode.MISSING_REQUIRED_PARAMETER, errorMsg);
  }

  // ✅ 커스텀 예외
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
    return buildErrorResponse(ex.getErrorCode(), ex.getMessage());
  }

  // ✅ 그 외 알 수 없는 예외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
    return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  // ✅ 공통 응답 생성 (ApiResponse 사용)
  private ResponseEntity<ApiResponse<Void>> buildErrorResponse(BaseErrorCode errorCode, String customMessage) {
    ApiResponse<Void> response = ApiResponse.error(errorCode, customMessage);
    return new ResponseEntity<>(response, errorCode.getHttpStatus());
  }
}
