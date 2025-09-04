package com.stopusing_BE.global.config;

import com.stopusing_BE.global.jwt.JWTFilter;
import com.stopusing_BE.global.jwt.JWTUtils;
import com.stopusing_BE.global.oauth.CustomSuccessHandler;
import com.stopusing_BE.global.oauth.service.CustomOAuth2UserService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private static final String[] SWAGGER_WHITELIST = {
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/webjars/**",
      "/favicon.ico",
        "/api/v1/**"
    };

  // CORS 설정
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowCredentials(true);
    // 정확 매칭
    cfg.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5173","https://stopusing.klr.kr"));
    // 또는 패턴 허용(선택) — 127.0.0.1, 포트 변경 대비
    cfg.setAllowedOriginPatterns(List.of("http://localhost:*","http://127.0.0.1:*","https://stopusing.klr.kr"));

    // 개발 중에는 전체 허용이 덜 헷갈립니다
    cfg.addAllowedHeader("*");
    cfg.addAllowedMethod("*"); // GET, POST, PUT, PATCH, DELETE, OPTIONS 모두
    cfg.setExposedHeaders(List.of("Authorization","Location")); // 필요한 경우

    cfg.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      JWTUtils jwtUtils
  ) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterAfter(new JWTFilter(jwtUtils), OAuth2LoginAuthenticationFilter.class)
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(customOAuth2UserService))
            .successHandler(customSuccessHandler))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/oauth2/**", "/login/**", "/**").permitAll()
            .requestMatchers(SWAGGER_WHITELIST).permitAll()
            .requestMatchers("/v3/api-docs.yaml").permitAll()
            .anyRequest().authenticated()
        );


    return http.build();
  }
}
