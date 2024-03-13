package com.server.quant_bot.comm.security.controller;


import com.server.quant_bot.comm.security.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("로그인이 성공해야 한다.")
    @WithMockUser(username = "userId", roles = "ADMIN")
    void loginSuccess() throws Exception {

        final String REQUEST_JASON = "{\"userId\":\"userId\", \"password\": \"password\"}";

        mvc.perform(

                MockMvcRequestBuilders
                        .post( "/api/v1/login" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( REQUEST_JASON )
                        .with( csrf() )

        ).andExpect(status().isOk());
    }
}