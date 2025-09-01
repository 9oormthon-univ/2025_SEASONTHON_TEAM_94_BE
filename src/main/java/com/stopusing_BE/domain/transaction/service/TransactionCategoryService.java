package com.stopusing_BE.domain.transaction.service;

import com.stopusing_BE.domain.transaction.dto.response.TransactionCategoryResponse;
import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionCategoryService {

  @Transactional(readOnly = true)
  public List<TransactionCategoryResponse> getAll() {
    return Arrays.stream(TransactionCategory.values())
        .map(v -> TransactionCategoryResponse.builder()
            .value(v.name())
            .label(v.getLabel())
            .build())
        .toList();
  }
}