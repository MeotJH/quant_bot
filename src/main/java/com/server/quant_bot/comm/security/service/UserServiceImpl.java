package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(String userId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Override
    public UserEntity initUser() {
        UserEntity entity = new UserEntity();
        entity = (UserEntity) entity.update(
                new UserDto("userId", "{bcrypt}$2a$10$ngsfScZHJl4UvZEpfMZQJ.8kC8qcPJcJCYkJcf1x2T6/vWV42ocqi")
        );
        return userRepository.save(entity);
    }
}
