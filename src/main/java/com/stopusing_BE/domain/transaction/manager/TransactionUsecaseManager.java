package com.stopusing_BE.domain.transaction.manager;

import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.category.service.CategoryService;
import com.stopusing_BE.domain.transaction.dto.request.TransactionCreateRequest;
import com.stopusing_BE.domain.transaction.dto.request.TransactionUpdateRequest;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.domain.transaction.entity.TransactionType;
import com.stopusing_BE.domain.transaction.service.TransactionService;
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
  private final CategoryService categoryService;
  private final TransactionService transactionService;

  @Transactional
  public TransactionResponse create(TransactionCreateRequest request) {
    User user = userService.getByIdOrThrow(request.getUserId());
    List<Category> categories = categoryService.getAllByIdsOrThrow(request.getCategoryIds());
    Transaction transaction = transactionService.create(user, categories, request);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public TransactionResponse getById(Long userId,Long id) {
    userService.getByIdOrThrow(userId);
    Transaction transaction = transactionService.getByIdOrThrow(id);
    return TransactionResponse.fromEntity(transaction);
  }

  @Transactional
  public List<TransactionResponse> getAllByType(Long userId,TransactionType type) {
    userService.getByIdOrThrow(userId);
    List<Transaction> transaction = transactionService.getAllByType(userId,type);
    return transaction.stream().map(TransactionResponse::fromEntity).collect(Collectors.toList());
  }

  @Transactional
  public Long getTotalPriceByType(Long userId,TransactionType type) {
    userService.getByIdOrThrow(userId);
    List<Transaction> transaction = transactionService.getAllByType(userId,type);
    return transaction.stream().map(Transaction::getPrice).reduce(0L, Long::sum);
  }

  @Transactional
  public TransactionResponse update(TransactionUpdateRequest request,Long userId,Long id) {
    userService.getByIdOrThrow(userId);
    List<Category> newCats = categoryService.getAllByIdsOrThrow(request.getCategoryIds());
    Transaction tx = transactionService.update(request, userId,id, newCats);
    return TransactionResponse.fromEntity(tx);
  }

  @Transactional
  public TransactionResponse delete(Long userId,Long id) {
    Transaction transaction = transactionService.deleteById(id, userId);
    return TransactionResponse.fromEntity(transaction);
  }




}

