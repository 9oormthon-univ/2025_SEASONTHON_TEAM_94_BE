package com.stopusing_BE.global.config;

import com.stopusing_BE.global.jwt.JWTFilter;
import com.stopusing_BE.global.jwt.JWTUtils;
import com.stopusing_BE.global.oauth.CustomSuccessHandler;
import com.stopusing_BE.global.oauth.service.CustomOAuth2UserService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
      "/favicon.ico"
    };

  // CORS 설정
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
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
        .addFilterBefore(new JWTFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
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
