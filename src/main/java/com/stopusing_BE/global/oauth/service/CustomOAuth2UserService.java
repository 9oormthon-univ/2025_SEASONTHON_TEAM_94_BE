package com.stopusing_BE.global.oauth.service;

import com.stopusing_BE.domain.user.dto.response.UserResponse;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.domain.user.repository.UserRepository;
import com.stopusing_BE.global.oauth.dto.response.CustomOAuth2UserResponse;
import com.stopusing_BE.global.oauth.dto.response.KakaoResponse;
import com.stopusing_BE.global.oauth.dto.response.OAuth2Response;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    OAuth2Response oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

    String username = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId();

    Optional<User> accountOpt = userRepository.findUserByUsername(username);

    if (accountOpt.isEmpty()) {
      User user = User.builder()
          .username(username)
          .nickname("new")
          .role("ROLE_USER")
          .build();

      userRepository.save(user);

      UserResponse userResponse = UserResponse.builder()
          .id(user.getUid())
          .username(username)
          .nickname(user.getNickname())
          .role(user.getRole())
          .build();


      return new CustomOAuth2UserResponse(userResponse);
    }

    User existUser = accountOpt.get();
    userRepository.save(existUser);

    System.out.println("userUid" + existUser.getUid());


    UserResponse userResponse = UserResponse.builder()
        .id(existUser.getUid())
        .username(username)
        .nickname(username)
        .role(existUser.getRole())
        .build();

    return new CustomOAuth2UserResponse(userResponse);
  }
}