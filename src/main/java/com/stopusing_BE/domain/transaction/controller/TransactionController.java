package com.stopusing_BE.domain.transaction.controller;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.manager.TransactionUsecaseManager;
import com.stopusing_BE.domain.transaction.spec.TransactionSpec;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
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
  @PostMapping
  public ApiResponse<TransactionResponse> create(TransactionCreateRequest request) {
        TransactionResponse transactionResponse = transactionManager.create(request);
        return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping
  public ApiResponse<List<TransactionResponse>> getAllByType(Long userId,TransactionType type) {
    List<TransactionResponse> transactionResponse = transactionManager.getAllByType(userId,type);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ApiResponse<TransactionResponse> getById(Long userId, Long id) {
    TransactionResponse transactionResponse = transactionManager.getById(userId,id);
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @GetMapping("total-price")
  public ApiResponse<Long> getTotalPriceByType(
      Long userId,
      TransactionType type
  ) {
    Long totalPrice = transactionManager.getTotalPriceByType(userId,type);
    return ApiResponse.success(totalPrice);
  }

  @Override
  @PutMapping("/{id}")
  public ApiResponse<TransactionResponse> update(
      TransactionUpdateRequest request
      ,Long userId,Long id) {
    TransactionResponse transactionResponse = transactionManager.update(
        request, userId, id
    );
    return ApiResponse.success(transactionResponse);
  }

  @Override
  @DeleteMapping("/{id}")
  public ApiResponse<TransactionResponse> delete(Long userId,Long id) {
    TransactionResponse deleted = transactionManager.delete(userId, id);
    return ApiResponse.success(deleted);
  }


}
