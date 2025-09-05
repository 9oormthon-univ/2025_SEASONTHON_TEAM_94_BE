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
import org.springframework.http.HttpHeaders;
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

    String token = jwtUtils.createJwt(userUid ,username, role, 60*60*1000L);

    // 로컬 환경 여부 체크
    boolean isLocal = isLocalReferer(request);

    // ResponseCookie 생성 및 추가
    ResponseCookie cookie = createCookie("Authorization", token);
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    response.sendRedirect( isLocal ? "https://localhost:5173/auth/callback/" : "https://stopusing.klr.kr/auth/callback/");
  }

  /** 쿠키 생성 메서드 */
  public ResponseCookie createCookie(String key, String value) {
    return ResponseCookie.from(key, value)
        .httpOnly(true)
        .secure(true)
        .sameSite("None")
        .maxAge(60 * 60)
        .path("/")
        .build();
  }

  /** 요청 정보를 기반으로 로컬 환경 여부 판단 */
  private boolean isLocalReferer(HttpServletRequest request) {
    return isLocalHost(request.getHeader("Host")) ||
           isLocalHost(request.getHeader("X-Forwarded-Host")) ||
           isLocalHost(request.getServerName()) ||
           isLocalHost(request.getHeader("Referer"));
  }

  /** 호스트 정보가 로컬 환경인지 확인 */
  private boolean isLocalHost(String host) {
    return host != null && (host.contains("localhost") || host.contains("127.0.0.1") || host.contains("kauth.kakao.com"));
  }
}