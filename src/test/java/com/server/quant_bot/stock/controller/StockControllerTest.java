package com.server.quant_bot.stock.controller;

import com.server.quant_bot.comm.security.config.SecurityConfig;
import com.server.quant_bot.stock.service.MarketService;
import com.server.quant_bot.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KoreaStockController.class)
@ComponentScan( basePackages = {"com.server.quant_bot"} )
@Import(SecurityConfig.class)
class StockControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    StockService stockService;

    @MockBean
    MarketService marketService;

    @Test
    @DisplayName("API호출시 상태 200을 가져온다.")
    void findStockByTicker() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/korea/삼성전자")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk()
        );
    }

    @Test
    @DisplayName("API호출시 상태 200을 가져온다.")
    void findStocksByStockLike() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/korea/stocks/삼성")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}