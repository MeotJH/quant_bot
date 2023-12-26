package com.server.quant_bot.comm.security.enums;

public enum Auth {

    AUTHORIZATION("Authorization");

    private String value;

    Auth(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
