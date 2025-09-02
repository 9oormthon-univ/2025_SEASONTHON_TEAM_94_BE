package com.stopusing_BE.domain.transaction.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stopusing_BE.domain.transaction.entity.TransactionCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CategoryEstimator {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CategoryEstimator() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 거래 제목을 기반으로 TransactionCategory를 추정합니다.
     *
     * @param title 거래 제목
     * @return 추정된 TransactionCategory, 추정 실패 시 OTHER 반환
     */
    public TransactionCategory estimateCategory(String title) {
        if (title == null || title.trim().isEmpty()) {
            return TransactionCategory.OTHER;
        }

        try {
            String prompt = createPrompt(title);
            String response = callOpenAI(prompt);
            return parseCategoryFromResponse(response);
        } catch (Exception e) {
            log.error("카테고리 추정 실패: {}", e.getMessage(), e);
            return TransactionCategory.OTHER;
        }
    }

    private String createPrompt(String title) {
        StringBuilder categories = new StringBuilder();
        for (TransactionCategory category : TransactionCategory.values()) {
            categories.append("- ").append(category.name())
                    .append(" (").append(category.getLabel()).append(")\n");
        }

        return String.format("""
                다음 거래 제목을 분석하여 가장 적절한 카테고리를 추정해주세요.

                거래 제목: "%s"

                사용 가능한 카테고리:
                %s

                응답 형식: 카테고리 이름만 반환 (예: FOOD)
                한국어 거래 제목을 분석하여 가장 적절한 카테고리를 선택하세요.
                """, title, categories.toString());
    }

    private String callOpenAI(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[]{
                Map.of("role", "user", "content", prompt)
        });
        requestBody.put("max_tokens", 50);
        requestBody.put("temperature", 0.1);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return extractContentFromResponse(response.getBody());
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim();
        } catch (Exception e) {
            log.error("OpenAI 응답 파싱 실패: {}", e.getMessage());
            throw new RuntimeException("OpenAI 응답 파싱 실패", e);
        }
    }

    private TransactionCategory parseCategoryFromResponse(String response) {
        try {
            // 응답에서 카테고리 이름만 추출
            String categoryName = response.toUpperCase().trim();

            // 유효한 카테고리인지 확인
            for (TransactionCategory category : TransactionCategory.values()) {
                if (category.name().equals(categoryName)) {
                    return category;
                }
            }

            log.warn("알 수 없는 카테고리 응답: {}", response);
            return TransactionCategory.OTHER;
        } catch (Exception e) {
            log.error("카테고리 파싱 실패: {}", e.getMessage());
            return TransactionCategory.OTHER;
        }
    }
}