package com.stopusing_BE.global.dev;

import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.service.UserService;
import com.stopusing_BE.global.jwt.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
@Tag(name = "Development", description = "개발용 API")
public class DevController {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "개발용 로그인", description = "개발/테스트용 JWT 토큰을 발급합니다. userUid에 해당하는 실제 유저 정보로 토큰을 생성합니다.")
    public String devLogin(
            @RequestParam String userUid,
            HttpServletResponse response
    ) {
        // 실제 유저 정보 조회 (없으면 CustomException 발생)
        User user = userService.getByIdOrThrow(userUid);
        
        String token = jwtUtils.createJwt(user.getUid(), user.getUsername(), user.getRole(), 60*60*60L);
        
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge(60 * 60 * 60 * 10)
                .path("/")
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        return String.format("Dev login successful for user: %s (role: %s). Token: %s", 
                user.getUsername(), user.getRole(), token);
    }
}