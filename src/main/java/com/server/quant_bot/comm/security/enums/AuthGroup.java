package com.server.quant_bot.comm.security.enums;

import lombok.Getter;

@Getter
public enum AuthGroup {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String value;

    AuthGroup(String value){
        this.value = value;
    }
}
