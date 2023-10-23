package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataTrendFollowingService implements TrendFollowing{

    private final StockService stockService;
    private final int TREND_FOLLOIWNG_DEFAULT_DAY = 75;
    @Override
    public TrendFollowDto get(String ticker, String baseDt) {
        //TODO 함수화 리팩토링

        //baseDt yyyymmdd
        LocalDate date = LocalDate.parse(baseDt, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate beginDate = date.minusDays(TREND_FOLLOIWNG_DEFAULT_DAY);
        String beginDateStr = beginDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<PublicDataStockDto> allByAfterBeginDate = stockService.getAllByAfterBeginDate(ticker, beginDateStr);

        double temp = 0.0;
        for (PublicDataStockDto each: allByAfterBeginDate) {
            temp = temp + Double.parseDouble(each.getClpr());
        }
        double trendFollowPrice = temp / allByAfterBeginDate.size();
        double baseDateClosePrice = Double.parseDouble(allByAfterBeginDate.get(0).getClpr());

        log.info(trendFollowPrice + " ::: TrendFollowPrice");
        log.info(baseDateClosePrice + " ::: TodayClosePrice");

        boolean isBuy = trendFollowPrice < baseDateClosePrice;
        return TrendFollowDto
                .builder()
                .trendFollowPrice(trendFollowPrice)
                .baseDateClosePrice(baseDateClosePrice)
                .isBuy(isBuy)
                .build();
    }
}
