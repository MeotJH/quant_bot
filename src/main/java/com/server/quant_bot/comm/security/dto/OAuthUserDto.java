package com.server.quant_bot.comm.security.dto;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;


@Getter
public class OAuthUserDto extends DefaultOAuth2User {
    private String role;
    private String email;


    public OAuthUserDto(Collection<? extends GrantedAuthority> authorities
            , Map<String, Object> attributes
            , String nameAttributeKey
            , String role
            , String email
    ) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.email = email;
    }
}
