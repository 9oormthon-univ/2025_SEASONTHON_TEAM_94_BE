package com.stopusing_BE.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("stopusing API")
            .description("stopusing Swagger 문서")
            .version("v1.0.0"))

//         보안 설정 추가 (JWT 토큰 인증 방식 명시)
        .addSecurityItem(new SecurityRequirement().addList("JWT"))

        // JWT 인증 방식에 대한 세부 설정
        .components(new Components().addSecuritySchemes("JWT",
            new SecurityScheme()
                .name("JWT")
                .type(SecurityScheme.Type.HTTP) // HTTP 방식의 인증 사용
                .scheme("Bearer") // 'Bearer' 인증 방식 (토큰 기반)
                .bearerFormat("JWT") // JWT 형식 사용 명시
        ));


  }
}
