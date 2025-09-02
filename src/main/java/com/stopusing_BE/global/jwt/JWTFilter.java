package com.stopusing_BE.global.jwt;

import com.stopusing_BE.domain.user.dto.response.UserResponse;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
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

    //토큰 소멸 시간 검증
    if (jwtUtils.isExpired(token)) {
      filterChain.doFilter(request, response);
      //조건이 해당되면 메소드 종료 (필수)
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
  }



}
