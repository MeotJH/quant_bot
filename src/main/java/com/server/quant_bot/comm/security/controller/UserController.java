package com.server.quant_bot.comm.security.controller;

import com.server.quant_bot.comm.security.mapper.UserMapper;
import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.comm.security.view.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserView findUser(){
        return UserMapper
                .INSTANCE
                .userEntityToView(
                        userService
                                .findUserByLoginId()
                                .get()
                );
    }
}
