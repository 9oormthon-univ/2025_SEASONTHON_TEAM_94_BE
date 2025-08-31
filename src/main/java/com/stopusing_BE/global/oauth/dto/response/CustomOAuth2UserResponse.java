package com.stopusing_BE.global.oauth.dto.response;

import com.stopusing_BE.domain.user.dto.response.UserResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2UserResponse implements OAuth2User {

  private final UserResponse userResponse;

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(userResponse::getRole);
    return authorities;
  }

  @Override
  public String getName() {
    return userResponse.getNickname();
  }

  public Long getUserId() {
    return userResponse.getId();
  }

  public String getUserName() {
    return userResponse.getUsername();
  }


}