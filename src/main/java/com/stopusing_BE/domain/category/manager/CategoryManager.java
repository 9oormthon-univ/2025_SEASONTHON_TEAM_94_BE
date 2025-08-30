package com.stopusing_BE.domain.category.manager;

import com.stopusing_BE.domain.category.dto.request.CategoryCreateRequest;
import com.stopusing_BE.domain.category.dto.response.CategoryResponse;
import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.category.service.CategoryService;
import com.stopusing_BE.domain.transaction.dto.response.TransactionResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CategoryManager {
  private final CategoryService categoryService;

  @Transactional
  public CategoryResponse create(CategoryCreateRequest req) {
    Category saved = categoryService.create(req);
    return CategoryResponse.fromEntity(saved);
  }

  @Transactional
  public List<CategoryResponse> getAll() {
    List<Category> categories = categoryService.getAll();
    return categories.stream().map(CategoryResponse::fromEntity).collect(Collectors.toList());
  }
}
