package com.server.quant_bot.comm.security.config;

import com.server.quant_bot.comm.security.filter.JwtAuthenticationFilter;
import com.server.quant_bot.comm.security.handler.CustomAccessDeniedHandler;
import com.server.quant_bot.comm.security.handler.CustomAuthenticationEntryPoint;
import com.server.quant_bot.comm.security.handler.OAuth2LoginSuccessHandler;
import com.server.quant_bot.comm.security.service.CustomUserDetailsService;
import com.server.quant_bot.comm.security.service.JwtTokenProvider;
import com.server.quant_bot.comm.security.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

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
                                                 ,antMatcher("/api/**/**")
                                                 ,antMatcher("/")
                                                 ,antMatcher("/view/**")
                                                 // swagger 를 위한 url open
                                                 ,antMatcher("/swagger-ui/**")
                                                 ,antMatcher("/api-docs/**"))
                                    .permitAll()
                                .requestMatchers(antMatcher("/api/**/auth/**")
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
                )
                .oauth2Login( oauth2 -> oauth2
                        .loginPage("/view/login")
                        .successHandler(oAuth2LoginSuccessHandler)
                        .userInfoEndpoint( userInfo -> userInfo.userService(oAuth2UserService) )
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
