package com.stopusing_BE.domain.category.service;

import com.stopusing_BE.domain.category.dto.request.CategoryCreateRequest;
import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.category.exception.CategoryErrorCode;
import com.stopusing_BE.domain.category.repository.CategoryRepository;
import com.stopusing_BE.global.common.exception.CustomException;
import com.stopusing_BE.global.common.exception.code.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<Category> getAllByIdsOrThrow(List<Long> ids) {
    if (ids == null || ids.isEmpty()) return List.of();
    List<Category> found = categoryRepository.findAllById(ids);
    if (found.size() != ids.size()) {
      throw new CustomException(CategoryErrorCode.CATEGORY_ERROR_CODE);
    }
    return found;
  }

  @Transactional
  public Category create(CategoryCreateRequest req) {
    String name = req.getName().trim();

    if (categoryRepository.existsByName(name)) {
      throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 카테고리: " + name);
    }
    Category category = Category.builder()
        .name(name)
        .build();
    return categoryRepository.save(category);
  }

  @Transactional(readOnly = true)
  public List<Category> getAll() {
    return categoryRepository.findAll();
  }

}
