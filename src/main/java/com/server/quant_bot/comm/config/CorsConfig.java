package com.server.quant_bot.comm.config;

import com.server.quant_bot.comm.filter.CorsOriginLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://quant-bot.iptime.org");
    }

    @Bean
    public CorsOriginLoggingFilter corsOriginLoggingFilter() {
        return new CorsOriginLoggingFilter();
    }
}
