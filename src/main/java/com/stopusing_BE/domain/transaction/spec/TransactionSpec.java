package com.stopusing_BE.domain.transaction.spec;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateByAlertRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionTypeUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCalendarResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionReportResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransactionSpec {
  @Operation(summary = "알림 내역으로 지출 내역 생성", description = "알림 내역으로 지출 내역을 생성합니다.")
  ApiResponse<TransactionResponse> createByAlert(
      @RequestBody @Validated TransactionCreateByAlertRequest request
  );

  @Operation(summary = "지출 내역 생성", description = "지출 내역을 생성합니다.")
  ApiResponse<TransactionResponse> create(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @RequestBody @Validated TransactionCreateRequest request
  );



  @Operation(summary = "지출 내역 조회", description = "지출 내역을 type 별로 조회합니다.")
  ApiResponse<List<TransactionResponse>> getAllByTypeAndDateRange(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @RequestParam() TransactionType type,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startAt,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endAt
  );

  @Operation(summary = "지출 내역 상세 조회", description = "지출 내역 상세 정보를 조회합니다.")
  ApiResponse<TransactionResponse> getById(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @PathVariable Long id
  );

  @Operation(summary = "지출 분석을 위한 조회", description = "지출 분석을 위한 조회합니다.")
  ApiResponse<TransactionReportResponse> getAllForReportByType(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @RequestParam TransactionType type,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startAt,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endAt
  );

  @Operation(summary = "지출 캘린더를 위한 월별 내역 조회", description = "지출 캘린더를 위한 월별 내역 조회")
  ApiResponse<TransactionCalendarResponse> getTotalPricesByType(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @RequestParam TransactionType type,
      @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date
  );



  @Operation(summary = "지출 내역 수정", description = "지출 내역을 수정합니다.")
  ApiResponse<TransactionResponse> update(
      @RequestBody TransactionUpdateRequest request,
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @PathVariable Long id
  );

  @Operation(summary = "지출 내역 삭제", description = "지출 내역을 삭제합니다.")
  ApiResponse<TransactionResponse> delete(
      @AuthenticationPrincipal CustomOAuth2UserResponse currentUser,
      @PathVariable Long id
  );

  @Operation(summary = "카테고리 내역 조회", description = "카테고리 내역 조회 입니다.")
  ApiResponse<List<TransactionCategoryResponse>> getAllCategories();





  @Operation(summary = "Transaction Type 변경", description = "알림으로 생성된 Transaction의 Type을 변경합니다.")
  ApiResponse<TransactionResponse> updateTypeByAlert(
      @RequestBody @Validated TransactionTypeUpdateRequest request
  );

}
