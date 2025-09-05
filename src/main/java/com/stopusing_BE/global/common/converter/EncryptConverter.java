package com.stopusing_BE.global.common.converter;

import com.stopusing_BE.domain.transaction.util.EncryptUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

  private final EncryptUtil encryptUtil;

  @Value("${encrypt.converter.enabled:true}")
  private boolean enabled;

  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (attribute == null) return null;
    if (!enabled) return attribute;            // 🔴 비활성화 시 그대로 저장
    return encryptUtil.encryptContent(attribute);
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    if (!enabled) return dbData;               // 🔴 비활성화 시 그대로 조회
    return encryptUtil.decryptContent(dbData);
  }
}
