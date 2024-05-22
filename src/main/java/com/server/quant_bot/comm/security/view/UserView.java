package com.server.quant_bot.comm.security.view;

import java.util.List;

public record UserView(String userId, List<String> roles) {
}
