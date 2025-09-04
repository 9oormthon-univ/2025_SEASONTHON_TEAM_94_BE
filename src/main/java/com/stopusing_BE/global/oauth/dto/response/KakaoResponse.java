package com.stopusing_BE.global.oauth.dto.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
  private final Map<String, Object> attribute;
  private final Map<String, Object> properties;
  private final Map<String, Object> kakaoAccount;

  public KakaoResponse(Map<String, Object> attribute) {
    this.attribute = attribute;
    this.properties = (Map<String, Object>) attribute.get("properties");
    this.kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getProviderId() {
    return attribute.get("id").toString();
  }

  @Override
  public String getNickname() {
    return properties.get("nickname").toString();
  }

  @Override
  public String getEmail() {
    return kakaoAccount != null && kakaoAccount.get("email") != null
        ? kakaoAccount.get("email").toString()
        : null;
  }
}
