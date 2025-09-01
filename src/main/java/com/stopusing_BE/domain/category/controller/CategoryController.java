//package com.stopusing_BE.domain.category.controller;
//
//import com.stopusing_BE.domain.category.dto.request.CategoryCreateRequest;
//import com.stopusing_BE.domain.category.dto.response.CategoryResponse;
//import com.stopusing_BE.domain.category.manager.CategoryManager;
//import com.stopusing_BE.domain.category.spec.CategorySpec;
//import com.stopusing_BE.global.common.exception.response.ApiResponse;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/categories")
//@RequiredArgsConstructor
//public class CategoryController implements CategorySpec {
//  private final CategoryManager categoryManager;
//
//  @Override
//  @PostMapping
//  public ApiResponse<CategoryResponse> create(CategoryCreateRequest request) {
//    CategoryResponse categoryResponse = categoryManager.create(request);
//    return ApiResponse.success(categoryResponse);
//  }
//
//  @Override
//  @GetMapping
//  public ApiResponse<List<CategoryResponse>> getAll() {
//    List<CategoryResponse> categoryResponses = categoryManager.getAll();
//    return ApiResponse.success(categoryResponses);
//  }
//}
