package com.server.quant_bot.comm.config;

import org.springframework.beans.factory.annotation.Value;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    private final String API_KEY;
    private final String API_SECRET;

    MessageConfig(@Value("${message.api-key}") String apiKey, @Value("${message.api-secret}") String apiSecret){
        this.API_KEY = apiKey;
        this.API_SECRET = apiSecret;
    }

    @Bean
    public DefaultMessageService messageService() {
        String DOMAIN = "https://api.solapi.com";
        return NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET, DOMAIN);
    }
}
