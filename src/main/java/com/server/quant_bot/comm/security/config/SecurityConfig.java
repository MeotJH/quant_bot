package com.server.quant_bot.comm.security.config;

import com.server.quant_bot.comm.security.filter.JwtAuthenticationFilter;
import com.server.quant_bot.comm.security.handler.CustomAccessDeniedHandler;
import com.server.quant_bot.comm.security.handler.CustomAuthenticationEntryPoint;
import com.server.quant_bot.comm.security.service.CustomUserDetailsService;
import com.server.quant_bot.comm.security.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                //security 베이직 옵션 안쓸꺼야 -> restApi니깐
                .httpBasic(HttpBasicConfigurer::disable)

                //csrf off 해줘  -> restApi니깐
                .csrf(CsrfConfigurer::disable)

                //세션정책은 사용하지 않을것 같아 STATELESS(무상태)로 할거야
                .sessionManagement( sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )

                /**
                 * url css,js,root,view 페이지 전부허용
                 * url "/api/v1/**"은 전부허용
                 * url "/api/v1/auth/**"는 USER권한
                 * 그외 URL은 전부 권한 필요햬!
                 */
                .authorizeRequests( authorizeRequests ->
                        authorizeRequests
                                .requestMatchers( antMatcher("/css/**")
                                                 ,antMatcher("/js/**")
                                                 ,antMatcher("/assets/**")
                                                 ,antMatcher("/api/v1/**")
                                                 ,antMatcher("/")
                                                 ,antMatcher("/view/**") )
                                    .permitAll()
                                .requestMatchers(antMatcher("/api/v1/auth/**")
                                                 ,antMatcher("/auth/**"))
                                    .hasRole("USER")
                                .anyRequest()
                                    .authenticated()
                )
                //  JwtAuthenticationFilter 이걸 UsernamePasswordAuthenticationFilter 이거 전에 실행할거야
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                                            .accessDeniedHandler(customAccessDeniedHandler)
                                                            .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
