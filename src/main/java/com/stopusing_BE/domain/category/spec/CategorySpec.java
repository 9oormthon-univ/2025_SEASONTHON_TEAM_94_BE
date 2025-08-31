package com.stopusing_BE.domain.category.spec;

import com.stopusing_BE.domain.category.dto.request.CategoryCreateRequest;
import com.stopusing_BE.domain.category.dto.response.CategoryResponse;
import com.stopusing_BE.global.common.exception.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategorySpec {
  @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
  ApiResponse<CategoryResponse> create(
      @RequestBody CategoryCreateRequest request
  );

  @Operation(summary = "카테고리 조회", description = "카테고리를 조회합니다.")
  ApiResponse<List<CategoryResponse>> getAll();



}
