package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new ResourceCommException("해당하는 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(UserEntity userEntity) {
        return User.builder()
                .username(userEntity.getUserId())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().toArray(new String[0]))
                .build();
    }
}
