package com.stopusing_BE.global.jwt;

import com.stopusing_BE.domain.user.dto.response.UserResponse;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtils jwtUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
    String authorization = null;
    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("Authorization")) {
          authorization = cookie.getValue();
        }
      }
    }

    String requestUri = request.getRequestURI();

    // Swagger 경로 제외
    if (requestUri.startsWith("/swagger-ui") || 
        requestUri.startsWith("/v3/api-docs") || 
        requestUri.startsWith("/swagger-resources") || 
        requestUri.startsWith("/webjars") ||
        requestUri.equals("/favicon.ico")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

      filterChain.doFilter(request, response);
      return;
    }
    if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

      filterChain.doFilter(request, response);
      return;
    }

    //Authorization 헤더 검증
    if (authorization == null) {
      filterChain.doFilter(request, response);

      //조건이 해당되면 메소드 종료 (필수)
      return;
    }

    //토큰
    String token = authorization;

    //토큰 파싱 및 검증
    try {
      //토큰 소멸 시간 검증
      if (jwtUtils.isExpired(token)) {
        handleExpiredToken(request, response);
        return;
      }

      //토큰에서 username과 role 획득
      String userUid = jwtUtils.getUserUid(token);
      String username = jwtUtils.getUsername(token);
      String role = jwtUtils.getRole(token);

      UserResponse userResponse = UserResponse.builder()
          .id(userUid)
          .username(username)
          .role(role)
          .build();

      //UserDetails에 회원 정보 객체 담기
      CustomOAuth2UserResponse customOAuth2UserResponse = new CustomOAuth2UserResponse(userResponse);

      //스프링 시큐리티 인증 토큰 생성
      Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2UserResponse, null, customOAuth2UserResponse.getAuthorities());
      //세션에 사용자 등록
      SecurityContextHolder.getContext().setAuthentication(authToken);

      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      handleExpiredToken(request, response);
      return;
    } catch (Exception e) {
      // 토큰이 유효하지 않은 경우
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
  }

  private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // 만료된 쿠키 삭제
    Cookie expiredCookie = new Cookie("Authorization", null);
    expiredCookie.setMaxAge(0);
    expiredCookie.setPath("/");
    expiredCookie.setHttpOnly(true);
    expiredCookie.setSecure(true);
    response.addCookie(expiredCookie);
    
    // 환경에 따른 OAuth2 로그인 URL 생성
    String oauth2LoginUrl = getOAuth2LoginUrl(request);
    
    // Accept 헤더를 확인하여 API 요청인지 판단
    String acceptHeader = request.getHeader("Accept");
    boolean isApiRequest = acceptHeader != null && acceptHeader.contains("application/json");
    
    if (isApiRequest) {
      // API 요청인 경우 JSON 응답으로 OAuth2 로그인 URL 제공
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\",\"message\":\"JWT token has expired\",\"oauth2LoginUrl\":\"" + oauth2LoginUrl + "\"}");
    } else {
      // 일반 브라우저 요청인 경우 OAuth2 로그인으로 리다이렉트
      response.sendRedirect(oauth2LoginUrl);
    }
  }
  
  private String getOAuth2LoginUrl(HttpServletRequest request) {
    // 로컬 환경 여부 체크 (CustomSuccessHandler와 동일한 로직)
    boolean isLocal = isLocalReferer(request);
    
    if (isLocal) {
      return "/oauth2/authorization/kakao?redirect_uri=https://localhost:5173/auth/callback/";
    } else {
      return "/oauth2/authorization/kakao?redirect_uri=https://stopusing.klr.kr/auth/callback/";
    }
  }
  
  private boolean isLocalReferer(HttpServletRequest request) {
    return isLocalHost(request.getHeader("Host")) ||
           isLocalHost(request.getHeader("X-Forwarded-Host")) ||
           isLocalHost(request.getServerName()) ||
           isLocalHost(request.getHeader("Referer"));
  }

  private boolean isLocalHost(String host) {
    return host != null && (host.contains("localhost") || host.contains("127.0.0.1") || host.contains("kauth.kakao.com"));
  }
}
