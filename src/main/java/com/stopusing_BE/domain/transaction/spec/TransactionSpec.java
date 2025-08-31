package com.stopusing_BE.domain.transaction.spec;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransactionSpec {
  @Operation(summary = "지출 내역 생성", description = "지출 내역을 생성합니다.")
  ApiResponse<TransactionResponse> create(
      @RequestBody TransactionCreateRequest request
  );

  @Operation(summary = "지출 내역 조회", description = "지출 내역을 type 별로 조회합니다.")
  ApiResponse<List<TransactionResponse>> getAllByType(
      @RequestParam() Long userId,
      @RequestParam(required = false) TransactionType type
  );

  @Operation(summary = "지출 내역 상세 조회", description = "지출 내역 상세 정보를 조회합니다.")
  ApiResponse<TransactionResponse> getById(
      @RequestParam Long userId,
      @PathVariable Long id
  );

  @Operation(summary = "타입별 지출 총 금액 조회", description = "타입별 지출 총 금액 조회합니다.")
  ApiResponse<Long> getTotalPriceByType(
      @RequestParam Long userId,
      @RequestParam TransactionType type
  );

  @Operation(summary = "지출 내역 수정", description = "지출 내역을 수정합니다.")
  ApiResponse<TransactionResponse> update(
      @RequestBody TransactionUpdateRequest request,
      @RequestParam() Long userId,
      @PathVariable Long id
  );

  @Operation(summary = "지출 내역 삭제", description = "지출 내역을 삭제합니다.")
  ApiResponse<TransactionResponse> delete(
      @RequestParam() Long userId,
      @PathVariable Long id
  );


}
