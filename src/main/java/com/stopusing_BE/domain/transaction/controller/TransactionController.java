package com.stopusing_BE.domain.transaction.controller;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateByAlertRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionReportResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.manager.TransactionUsecaseManager;
import com.stopusing_BE.domain.transaction.spec.TransactionSpec;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController implements TransactionSpec {

  private final TransactionUsecaseManager transactionManager;

  @Override
  @PostMapping("/alarm")
  public ApiResponse<TransactionResponse> createByAlert(TransactionCreateByAlertRequest request) {
        TransactionResponse transactionResponse = transactionManager.createByAlert(request);
        return ApiResponse.success(transactionResponse);
  }

  @Override
  @PostMapping
  public ApiResponse<TransactionResponse> create(TransactionCreateRequest request) {
    TransactionResponse transactionResponse = transactionManager.create(request);
    return ApiResponse.success(transactionResponse);
  }


  @Override
  @GetMapping
  public ApiResponse<List<TransactionResponse>> getAllByTypeAndDateRange(String userUid,TransactionType type, LocalDate startAt, LocalDate endAt) {
    List<TransactionResponse> transactionResponse = transactionManager.getAllByTypeAndRange(userUid,type, startAt, endAt);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ApiResponse<TransactionResponse> getById(String userUid, Long id) {
    TransactionResponse transactionResponse = transactionManager.getById(userUid,id);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("/report")
  public ApiResponse<TransactionReportResponse> getAllForReportByType(
      String userUid,
      TransactionType type,
      LocalDate startAt,
      LocalDate endAt
  ) {
    List<TransactionResponse> list = transactionManager.getAllByTypeAndRange(userUid, type, startAt, endAt);
    TransactionReportResponse transactionReportResponse = transactionManager.buildReport(list);
    return ApiResponse.success(transactionReportResponse);
  }

  @Override
  @PutMapping("/{id}")
  public ApiResponse<TransactionResponse> update(
      TransactionUpdateRequest request
      ,String userUid,Long id) {
    TransactionResponse transactionResponse = transactionManager.update(
        request, userUid, id
    );
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @DeleteMapping("/{id}")
  public ApiResponse<TransactionResponse> delete(String userUid,Long id) {
    TransactionResponse deleted = transactionManager.delete(userUid, id);
    return ApiResponse.success(deleted);
  }

  @Override
  @GetMapping("/categories")
  public ApiResponse<List<TransactionCategoryResponse>> getAllCategories() {
    List<TransactionCategoryResponse> response = transactionManager.getAllCategories();
    return ApiResponse.success(response);
  }


}
