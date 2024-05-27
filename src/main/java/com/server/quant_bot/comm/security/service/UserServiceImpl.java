package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.dto.OAuthUserDto;
import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(String userId, String password, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        setRefreshTokenToCookieHttpOnly(response,tokenInfo);

        return tokenInfo;
    }
    @Override
    public UserEntity initUser() {
        UserEntity entity = new UserEntity();

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        entity = (UserEntity) entity.update(
                new UserDto("userId"
                         , "{bcrypt}$2a$10$ngsfScZHJl4UvZEpfMZQJ.8kC8qcPJcJCYkJcf1x2T6/vWV42ocqi"
                        ,  roles)
        );
        Optional<UserEntity> byUserId = userRepository.findByUserId("userId");
        if(byUserId.isPresent()){
            return byUserId.get();
        }
        return userRepository.save(entity);
    }

    @Override
    public Optional<UserEntity> findUserByLoginId(String loginId) {
        return userRepository.findByUserId(loginId);
    }

    @Override
    public UserEntity initUser(OAuthUserDto oAuthUserDto) {
        UserEntity entity = new UserEntity();
        entity.updateByOauth(oAuthUserDto);
        return userRepository.save(entity);
    }

    @Override
    public Optional<UserEntity> findUserByLoginId() {
        log.info(":::findUserByLoginId:::{}",SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> byUserId = userRepository.findByUserId( principal.getUsername() );
        return byUserId;
    }


    private void setRefreshTokenToCookieHttpOnly(HttpServletResponse response, TokenInfo tokenInfo){
        Cookie cookie = new Cookie("RefreshToken",tokenInfo.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
