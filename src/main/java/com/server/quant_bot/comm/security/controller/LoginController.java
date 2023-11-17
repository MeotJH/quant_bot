package com.server.quant_bot.comm.security.controller;

import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {

    private final UserService userService;

    @PostMapping
    public TokenInfo login(@RequestBody UserDto userDto){
        return userService.login(userDto.userId(), userDto.password());
    }
}
