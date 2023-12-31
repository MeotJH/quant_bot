package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.trend_following.dto.*;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataTrendFollowingService implements TrendFollowing {

    private final StockService stockService;
    private final AuthTrendFollowing authTrendFollowing;
    private final int TREND_FOLLOIWNG_DEFAULT_DAY = 75;
    private final int TREND_FOLLOIWNGS_DEFAULT_DAY = 150;
    private final String DATE_TYPE_PATTERN = "yyyyMMdd";
    @Override
    public TrendFollowDto getOneday(String ticker, String baseDt) {
        String beginDateStr = getTrendFollowStartDate(baseDt);
        List<PublicDataStockDto> allByAfterBeginDate = stockService.getAllByAfterBeginDate(ticker, beginDateStr);

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

    @Override
    public TrendFollowListDto getDaysByBaseDt(String ticker, String baseDt) {
        String beginDateStr = getTrendFollowsStartDate(baseDt);
        List<PublicDataStockDto> allByAfterBeginDate = stockService.getAllByAfterBeginDate(ticker, beginDateStr);

        List<TrendFollowRecordForList> results = calculateTrendFollows(allByAfterBeginDate);
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
                            ,savedDay.getStockName()
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
        for (PublicDataStockDto each: allByAfterBeginDate) {
            temp = temp + Double.parseDouble(each.getClpr());
        }

        double trendFollowPrice = temp / allByAfterBeginDate.size();
        double baseDateClosePrice = Double.parseDouble(allByAfterBeginDate.get(0).getClpr());
        return new TrendFollowRecord(trendFollowPrice, baseDateClosePrice);
    }

    //TODO ㄹㅇ75개 데이터로 계산하게 바꾸자
    private List<TrendFollowRecordForList> calculateTrendFollows(List<PublicDataStockDto> allByAfterBeginDate) {

        //메소드로 나누자
        double temp = 0.0;
        int index = 0;

        String beginDateStr = getTrendFollowStartDate( allByAfterBeginDate.get(0).getBasDt() );
        String parseWeekendDate = changeWeekendDate(beginDateStr);

        for (PublicDataStockDto each: allByAfterBeginDate) {
            if( each.getBasDt().equals(parseWeekendDate) ){
                break;
            }// TODO 토,일 말고도 추석 등 이어서 휴장인날 어떻게 할지 고민해야 한다.
            temp = temp + Double.parseDouble(each.getClpr());
            index++;

        }

        double trendFollowPriceFirstOne = temp / index;
        double baseDateClosePriceFirstOne = Double.parseDouble(allByAfterBeginDate.get(0).getClpr());
        TrendFollowRecordForList trl = new TrendFollowRecordForList(trendFollowPriceFirstOne, baseDateClosePriceFirstOne,allByAfterBeginDate.get(0).getBasDt());
        List<TrendFollowRecordForList> trls = new ArrayList<>();
        trls.add(trl);

        //index번을 추가 안한거니까
        int j = 0;
        for (int i = index; i < allByAfterBeginDate.size(); i++) {
            PublicDataStockDto each = allByAfterBeginDate.get(index);

            //구해진 시작일 ~ n일 더한 값에 n일 종가를 더하고 시작일 종가를 뺀값을 더하면 이전 평균일이 나온다.
            temp = temp + ( Double.parseDouble(each.getClpr()) - Double.parseDouble(allByAfterBeginDate.get(j++).getClpr() ) );

            // 그날 평균이동값과 시작일+1일 종가를 구해서 넣어준다.
            double trendFollowTemp = temp / index;
            trls.add(
                    new TrendFollowRecordForList(
                            trendFollowTemp
                            , Double.parseDouble(allByAfterBeginDate.get(j).getClpr())
                            , allByAfterBeginDate.get(j).getBasDt()
                    )
            );

            if( allByAfterBeginDate.get(j).getBasDt().equals(beginDateStr) ){
                break;
            }

        }
        return trls;
    }

    private String changeWeekendDate(String beginDateStr) {
        String dayOfWeek = DateUtill.getDayOfWeek(beginDateStr);
        if(dayOfWeek.equals("토요일")){
            return DateUtill.getMinusDay(beginDateStr, 1);
        } else if (dayOfWeek.equals("일요일")) {
            return DateUtill.getMinusDay(beginDateStr,2);
        }else{
            return beginDateStr;
        }
    }

    private String getTrendFollowStartDate(String baseDt) {
        LocalDate date = LocalDate.parse(baseDt, DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
        return date.minusDays(TREND_FOLLOIWNG_DEFAULT_DAY).format(DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
    }


    private String getTrendFollowsStartDate(String baseDt) {
        LocalDate date = LocalDate.parse(baseDt, DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
        return date.minusDays(TREND_FOLLOIWNGS_DEFAULT_DAY).format(DateTimeFormatter.ofPattern(DATE_TYPE_PATTERN));
    }
}
