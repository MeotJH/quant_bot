package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.dto.OAuthUserDto;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.enums.AuthGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(AuthGroup.USER.getValue());

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthUserDto dto = new OAuthUserDto(
                authorities
                , oAuth2User.getAttributes()
                , userNameAttributeName
                , AuthGroup.USER.getValue()
                , "kakao"
        );
        String id = String.valueOf(oAuth2User.getAttributes().get("id"));

        //처음이면 회원가입
        if(!userService.findUserByLoginId(id).isPresent()){
            userService.initUser(dto);
        }
        return dto;
    }

}

