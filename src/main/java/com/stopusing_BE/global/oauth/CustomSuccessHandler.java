package com.stopusing_BE.global.oauth;

import com.stopusing_BE.global.jwt.JWTUtils;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JWTUtils jwtUtils;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    CustomOAuth2UserResponse customUserDetails = (CustomOAuth2UserResponse) authentication.getPrincipal();
    String username = customUserDetails.getUserName();
    String userUid = customUserDetails.getUserUid();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    String token = jwtUtils.createJwt(userUid ,username, role, 60*60*60L);

    response.addCookie(createCookie("Authorization", token));
    response.sendRedirect("https://stopusing.klr.kr/auth/callback");
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60*60*60);
    //cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(false);

    return cookie;
  }
}