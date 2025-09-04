package com.stopusing_BE.global.common.converter;

import com.stopusing_BE.domain.transaction.util.EncryptUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

  private final EncryptUtil encryptUtil;

  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (attribute == null) return null;
    return encryptUtil.encryptContent(attribute);
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    return encryptUtil.decryptContent(dbData);
  }
}
