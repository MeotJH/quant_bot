package com.server.quant_bot.comm.security.dto;

import java.util.List;

public record UserDto(String userId, String password, List<String> roles) {
}
