package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    default TokenInfo login(String memberId, String password, HttpServletResponse response) {return null;}

    default UserEntity initUser(){return null;}
}
