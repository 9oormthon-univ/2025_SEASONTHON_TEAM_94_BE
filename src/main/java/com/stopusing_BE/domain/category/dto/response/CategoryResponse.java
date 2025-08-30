package com.stopusing_BE.domain.category.dto.response;

import com.stopusing_BE.domain.category.entity.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
  private Long id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CategoryResponse fromEntity(Category c) {
    return CategoryResponse.builder()
        .id(c.getId())
        .name(c.getName())
        .createdAt(c.getCreatedAt())
        .updatedAt(c.getUpdatedAt())
        .build();
  }
}