package com.stopusing_BE.global.oauth.dto.response;

public interface OAuth2Response {
  String getProvider();

  String getProviderId();

  String getNickname();

  String getEmail();

//  String getImage();
}