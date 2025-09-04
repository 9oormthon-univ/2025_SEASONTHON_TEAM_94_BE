package com.stopusing_BE.domain.transaction.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncryptUtil {

  private final AesBytesEncryptor encryptor;

  // 암호화
  public String encryptContent(String content) {
    byte[] encrypt = encryptor.encrypt(content.getBytes(StandardCharsets.UTF_8));
    return byteArrayToString(encrypt);
  }

  // 복호화
  public String decryptContent(String encryptContent) {
    byte[] decryptBytes = stringToByteArray(encryptContent);
    byte[] decrypt = encryptor.decrypt(decryptBytes);
    return new String(decrypt, StandardCharsets.UTF_8);
  }

  // byte -> String
  private String byteArrayToString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte abyte : bytes) {
      sb.append(abyte);
      sb.append(" ");
    }
    return sb.toString();
  }

  // String -> byte
  private byte[] stringToByteArray(String byteString) {
    String[] split = byteString.split("\\s");
    ByteBuffer buffer = ByteBuffer.allocate(split.length);
    for (String s : split) {
      buffer.put((byte) Integer.parseInt(s));
    }
    return buffer.array();
  }
}

