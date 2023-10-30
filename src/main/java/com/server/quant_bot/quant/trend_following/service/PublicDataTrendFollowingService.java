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

    private final String DATE_TYPE_PATTERN = "yyyyMMdd";
    @Override
    public TrendFollowDto get(String ticker, String baseDt) {
        String beginDateStr = getTrendFollowStartDate(baseDt);
        List<PublicDataStockDto> allByAfterBeginDate = stockService.getAllByAfterBeginDate(ticker, beginDateStr);

        Result result = getResult(allByAfterBeginDate);

        log.info(result.trendFollowPrice() + " ::: TrendFollowPrice");
        log.info(result.baseDateClosePrice() + " ::: TodayClosePrice");

        boolean isBuy = result.trendFollowPrice() < result.baseDateClosePrice();
        return TrendFollowDto
                .builder()
                .trendFollowPrice(result.trendFollowPrice())
                .baseDateClosePrice(result.baseDateClosePrice())
                .isBuy(isBuy)
                .build();
    }

    private static Result getResult(List<PublicDataStockDto> allByAfterBeginDate) {
        double temp = 0.0;
        for (PublicDataStockDto each: allByAfterBeginDate) {
            temp = temp + Double.parseDouble(each.getClpr());
        }
        double trendFollowPrice = temp / allByAfterBeginDate.size();
        double baseDateClosePrice = Double.parseDouble(allByAfterBeginDate.get(0).getClpr());
        Result result = new Result(trendFollowPrice, baseDateClosePrice);
        return result;
    }

    private String getTrendFollowStartDate(String baseDt) {
        LocalDate date = LocalDate.parse(baseDt, DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
        return date.minusDays(TREND_FOLLOIWNG_DEFAULT_DAY).format(DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
    }

    private record Result(double trendFollowPrice, double baseDateClosePrice) {}
}
