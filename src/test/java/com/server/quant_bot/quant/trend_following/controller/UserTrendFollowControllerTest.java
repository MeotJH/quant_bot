package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.korea.controller.StockController;
import com.server.quant_bot.korea.service.MarketService;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserTrendFollowController.class)
class UserTrendFollowControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrendFollowing trendFollowing;
    
    @Test
    @DisplayName("보안이 되는지 확인해야한다")
    @WithMockUser(username = "failUser", roles = "USER")
    void checkAuthTest() throws Exception {
        final String REQUEST_JASON = "{\"userId\":\"userId\", \"password\": \"password\"}";

        mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/auth/trend-follow/삼성전자")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    
    }

}