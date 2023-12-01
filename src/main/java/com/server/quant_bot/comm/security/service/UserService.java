package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface UserService {

    default TokenInfo login(String memberId, String password, HttpServletResponse response) {return null;}

    default UserEntity initUser(){return null;}

    default Optional<UserEntity> findUserByLoginId(){return Optional.ofNullable(null);}

}
