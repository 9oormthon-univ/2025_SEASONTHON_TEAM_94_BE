package com.stopusing_BE.domain.transaction.manager;

import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.service.TransactionCategoryService;
import com.stopusing_BE.domain.transaction.service.TransactionService;
import com.stopusing_BE.domain.transaction.util.CategoryEstimator;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionUsecaseManager {

  private final UserService userService;
  private final TransactionService transactionService;
  private final TransactionCategoryService transactionCategoryService;
  private final CategoryEstimator categoryEstimator;

  @Transactional
  public TransactionResponse create(TransactionCreateRequest request) {
    User user = userService.getByIdOrThrow(request.getUserId());
    
    // AI를 통한 카테고리 추정
    TransactionCategory estimatedCategory = categoryEstimator.estimateCategory(request.getTitle());
    
    Transaction transaction = transactionService.create(user, request, estimatedCategory);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public TransactionResponse getById(String userUid,Long id) {
    userService.getByIdOrThrow(userUid);
    Transaction transaction = transactionService.getByIdOrThrow(id);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public List<TransactionResponse> getAllByType(String userUid,TransactionType type) {
    userService.getByIdOrThrow(userUid);
    List<Transaction> transaction = transactionService.getAllByType(userUid,type);
    return transaction.stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());
  }

  @Transactional
  public Long getTotalPriceByType(String userUid,TransactionType type) {
    userService.getByIdOrThrow(userUid);
    List<Transaction> transaction = transactionService.getAllByType(userUid,type);
    return transaction.stream().map(Transaction::getPrice).reduce(0L, Long::sum);
  }

  @Transactional
  public TransactionResponse update(TransactionUpdateRequest request,String userUid,Long id) {
    userService.getByIdOrThrow(userUid);
    Transaction tx = transactionService.update(request, userUid,id);
    return TransactionResponse.fromEntity(tx);
  }

  @Transactional
  public TransactionResponse delete(String userUid,Long id) {
    Transaction transaction = transactionService.deleteById(id, userUid);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional(readOnly = true)
  public List<TransactionCategoryResponse> getAllCategories() {
    return transactionCategoryService.getAll();
  }


}

