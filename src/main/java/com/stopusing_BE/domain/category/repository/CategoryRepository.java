package com.stopusing_BE.domain.category.repository;

import com.stopusing_BE.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
  boolean existsByName(String name); // 중복 방지용
}
