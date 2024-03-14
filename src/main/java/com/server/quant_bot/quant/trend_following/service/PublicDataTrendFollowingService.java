package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.enums.StockType;
import com.server.quant_bot.stock.event.StockServiceEvent;
import com.server.quant_bot.stock.service.StockService;
import com.server.quant_bot.quant.trend_following.dto.*;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class PublicDataTrendFollowingService implements TrendFollowing {

    private final Map<String,StockService> stockServices;
    private final StockService stockService;
    private final AuthTrendFollowing authTrendFollowing;
    private final int TREND_FOLLOIWNG_DEFAULT_DAY = 75;
    private final int TREND_FOLLOIWNGS_DEFAULT_DAY = 250;
    private final String DATE_TYPE_PATTERN = "yyyyMMdd";
    private StockType stockType = StockType.KOREA_STOCK;

    /**
     * ticker인 baseDt 날짜의 추세평균이동 값 가져온다.
     * @param ticker ex) 00239388
     * @param baseDt ex) yyyyMMdd
     * @return
     */
    @Override
    public TrendFollowDto getOneday(String ticker, String baseDt) {
        String beginDateStr = getTrendFollowsStartDate(baseDt);
        List<PublicDataStockDto> allByAfterBeginDate = getStockService().getAllByAfterBeginDate(ticker, beginDateStr);

        TrendFollowRecord result = calculateTrendFollowOne(allByAfterBeginDate);

        log.info(result.trendFollowPrice() + " ::: TrendFollowPrice");
        log.info(result.baseDateClosePrice() + " ::: TodayClosePrice");

        boolean isBuy = result.trendFollowPrice() < result.baseDateClosePrice();

        return TrendFollowDto
                .builder()
                .trendFollowPrice( getDoubleToMoney( result.trendFollowPrice() )  )
                .baseDateClosePrice( getDoubleToMoney( result.baseDateClosePrice() ) )
                .isBuy(isBuy)
                .build();
    }

    /**
     * 추세평균이동 값들을 리스트로 가져온다.
     * @param ticker ex) 00282732
     * @param baseDt ex) yyyyMMdd
     * @return TrendFollowListDto
     */
    @Override
    public TrendFollowListDto getDays(String ticker, String baseDt) {
        String beginDateStr = getTrendFollowsStartDate(baseDt);
        List<PublicDataStockDto> allByAfterBeginDate = getStockService().getAllByAfterBeginDate(ticker, beginDateStr);

        List<TrendFollowRecordForList> results = calculateTrendFollowsV2(allByAfterBeginDate);
        return toTrendFollowDtos(results);
    }

    @Override
    public List<String> getStocksByKeyword(String keyword) {
        return TrendFollowing.super.getStocksByKeyword(keyword);
    }

    @Override
    public Optional<TrendFollowDto> save(TrendFollowDto dto) {
        return authTrendFollowing.save(dto);
    }

    @Override
    public List<TrendFollowUserPageDto> findTrendDtoByUserId() {
        List<TrendFollowDto> trendDtoByUserId = authTrendFollowing.findTrendDtoByUserId();
        List<TrendFollowUserPageDto> responseDtos = new ArrayList<>();

        trendDtoByUserId.forEach( savedDay -> {

            TrendFollowDto today = getOneday(savedDay.getStockName(), DateUtill.getToday());
            responseDtos.add(

                    new TrendFollowUserPageDto(
                              savedDay.getStock()
                            , savedDay.getStockName()
                            , today.getBaseDateClosePrice()
                            , savedDay.getBaseDateClosePrice()
                            , today.getTrendFollowPrice()
                            , savedDay.getTrendFollowPrice()
                            , today.getIsBuy()
                            , savedDay.getIsBuy()
                            , savedDay.getApproval()
                    )

            );

        });

        return responseDtos;
    }

    @Override
    public Optional<TrendFollow> findByStock(Stock stock) {
        return authTrendFollowing.findByStock(stock);
    }

    private String getDoubleToMoney(Double target){
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format( Math.round( target * 100) / 100.0 );
    }

    private TrendFollowListDto toTrendFollowDtos(List<TrendFollowRecordForList> results){

        TrendFollowListDto dto = new TrendFollowListDto();
        for (TrendFollowRecordForList each: results) {

            boolean isBuy = each.trendFollowPrice() < each.baseDateClosePrice();
            dto.getTrendFollowPrices().add(each.trendFollowPrice());
            dto.getClosePrice().add(each.baseDateClosePrice());
            dto.getIsBuy().add(isBuy);
            dto.getBaseDt().add(each.baseDt());

        }
        return dto;
    }

    private TrendFollowRecord calculateTrendFollowOne(List<PublicDataStockDto> allByAfterBeginDate) {
        double temp = 0.0;
        for (int i = 0; i < TREND_FOLLOIWNG_DEFAULT_DAY; i++) {
            PublicDataStockDto each = allByAfterBeginDate.get(i);
            temp = temp + Double.parseDouble(each.getClpr());
        }

        double trendFollowPrice = temp / TREND_FOLLOIWNG_DEFAULT_DAY;
        double baseDateClosePrice = Double.parseDouble(allByAfterBeginDate.get(0).getClpr());
        return new TrendFollowRecord(trendFollowPrice, baseDateClosePrice);
    }

    private List<TrendFollowRecordForList> calculateTrendFollowsV2(List<PublicDataStockDto> allByAfterBeginDate) {
        TrendFollowRecord trendFollowRecord = calculateTrendFollowOne(allByAfterBeginDate);
        TrendFollowRecordForList trl = new TrendFollowRecordForList(
                                                                            trendFollowRecord.trendFollowPrice()
                                                                            , trendFollowRecord.baseDateClosePrice()
                                                                            ,allByAfterBeginDate.get(0).getBasDt()
                                                                    );
        List<TrendFollowRecordForList> trls = new ArrayList<>();
        trls.add(trl);

        //index번을 추가 안한거니까
        //맨처음
        int j = 0;
        //75번째
        int index = TREND_FOLLOIWNG_DEFAULT_DAY;
        double temp = trendFollowRecord.trendFollowPrice() * TREND_FOLLOIWNG_DEFAULT_DAY;
        for (int i = index; i < allByAfterBeginDate.size(); i++, j++) {
            PublicDataStockDto each = allByAfterBeginDate.get(index);

            //구해진 시작일 ~ n일 더한 값에 n일 종가를 더하고 시작일 종가를 뺀값을 더하면 이전 평균일이 나온다.
            temp = temp + ( Double.parseDouble(each.getClpr()) - Double.parseDouble(allByAfterBeginDate.get(j).getClpr() ) );
            // 그날 평균이동값과 시작일+1일 종가를 구해서 넣어준다.
            double trendFollowTemp = temp / index;
            trls.add(
                    new TrendFollowRecordForList(
                            trendFollowTemp
                            , Double.parseDouble(allByAfterBeginDate.get(j).getClpr())
                            , allByAfterBeginDate.get(j).getBasDt()
                    )
            );

            if( j == TREND_FOLLOIWNG_DEFAULT_DAY - 1 ){
                break;
            }

        }
        return trls;
    }


    private String getTrendFollowsStartDate(String baseDt) {
        LocalDate date = LocalDate.parse(baseDt, DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
        return date.minusDays(TREND_FOLLOIWNGS_DEFAULT_DAY).format(DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
    }

    private StockService getStockService(){
        if(stockType == null){
            return stockService;
        }
        return stockServices.get(stockType.STOCK_SERVICE);
    }

    @EventListener
    private void findStockService(StockServiceEvent event){
        this.stockType = event.getType();
    }
}
