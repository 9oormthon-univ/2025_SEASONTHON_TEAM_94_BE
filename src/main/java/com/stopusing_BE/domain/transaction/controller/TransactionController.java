package com.stopusing_BE.domain.transaction.controller;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateByAlertRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionTypeUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCalendarResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionReportResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.manager.TransactionUsecaseManager;
import com.stopusing_BE.domain.transaction.spec.TransactionSpec;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
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
  public ApiResponse<TransactionResponse> create(CustomOAuth2UserResponse currentUser,TransactionCreateRequest request) {
    String userUid = currentUser.getUserUid();
    TransactionResponse transactionResponse = transactionManager.create(userUid,request);
    return ApiResponse.success(transactionResponse);
  }


  @Override
  @GetMapping
  public ApiResponse<List<TransactionResponse>> getAllByTypeAndDateRange(
      CustomOAuth2UserResponse currentUser,TransactionType type, LocalDate startAt, LocalDate endAt) {
    String userUid = currentUser.getUserUid();
    List<TransactionResponse> transactionResponse = transactionManager.getAllByTypeAndRange(userUid,type, startAt, endAt);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ApiResponse<TransactionResponse> getById(
      CustomOAuth2UserResponse currentUser
      , Long id) {
    String userUid = currentUser.getUserUid();
    TransactionResponse transactionResponse = transactionManager.getById(userUid,id);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("/report")
  public ApiResponse<TransactionReportResponse> getAllForReportByType(
      CustomOAuth2UserResponse currentUser,
      TransactionType type,
      LocalDate startAt,
      LocalDate endAt
  ) {
    String userUid = currentUser.getUserUid();
    List<TransactionResponse> list = transactionManager.getAllByTypeAndRange(userUid, type, startAt, endAt);
    TransactionReportResponse transactionReportResponse = transactionManager.buildReport(list);
    return ApiResponse.success(transactionReportResponse);
  }

  @Override
  @GetMapping("/calendar")
  public ApiResponse<TransactionCalendarResponse> getTotalPricesByType(
      CustomOAuth2UserResponse currentUser, TransactionType type, LocalDate date) {
    String userUid = currentUser.getUserUid();
    TransactionCalendarResponse transactionCalendarResponse = transactionManager.getTotalPricesByType(userUid, type, date);
    return ApiResponse.success(transactionCalendarResponse);
  }


  @Override
  @PutMapping("/{id}")
  public ApiResponse<TransactionResponse> update(
      TransactionUpdateRequest request
      ,CustomOAuth2UserResponse currentUser,Long id) {
    String userUid = currentUser.getUserUid();
    TransactionResponse transactionResponse = transactionManager.update(
        request, userUid, id
    );
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @DeleteMapping("/{id}")
  public ApiResponse<TransactionResponse> delete(CustomOAuth2UserResponse currentUser,Long id) {
    String userUid = currentUser.getUserUid();
    TransactionResponse deleted = transactionManager.delete(userUid, id);
    return ApiResponse.success(deleted);
  }

  @Override
  @GetMapping("/categories")
  public ApiResponse<List<TransactionCategoryResponse>> getAllCategories() {
    List<TransactionCategoryResponse> response = transactionManager.getAllCategories();
    return ApiResponse.success(response);
  }

  @Override
  @PutMapping("/alarm")
  public ApiResponse<TransactionResponse> updateTypeByAlert(TransactionTypeUpdateRequest request) {
    TransactionResponse transactionResponse = transactionManager.updateTypeByAlert(request);
    return ApiResponse.success(transactionResponse);
  }

}
