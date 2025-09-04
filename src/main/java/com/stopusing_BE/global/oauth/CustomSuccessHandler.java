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
import org.springframework.http.ResponseCookie;
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

    // 로컬 환경 여부 체크
    boolean isLocal = true;

    // ResponseCookie 생성 및 추가
    ResponseCookie cookie = createCookie("Authorization", token, isLocal);
    response.addHeader("Set-Cookie", cookie.toString());

    response.sendRedirect( isLocalReferer(request) ? "http://localhost:5173/auth/callback/" : "https://stopusing.klr.kr/auth/callback/");
  }

  /** 쿠키 생성 메서드 */
  public ResponseCookie createCookie(String key, String value, boolean isLocal) {
    return ResponseCookie.from(key, value)
        .httpOnly(false)
        .secure(true)
        .sameSite(isLocal ? "None" : "Lax")
        .maxAge(60 * 60 * 60 * 10)
        .path("/")
        .build();
  }

  /** Referer를 기반으로 로컬 환경 여부 판단 */
  private boolean isLocalReferer(HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    if (referer == null || referer.isEmpty()) return false;
    return referer.contains("localhost") || referer.contains("127.0.0.1");
  }
}