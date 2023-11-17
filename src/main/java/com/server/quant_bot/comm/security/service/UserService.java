package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.entity.UserEntity;

public interface UserService {

    default TokenInfo login(String memberId, String password) {return null;}

    default UserEntity initUser(){return null;}
}
