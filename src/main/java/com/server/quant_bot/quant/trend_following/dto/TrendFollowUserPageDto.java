package com.server.quant_bot.quant.trend_following.dto;

/**
 *
 * @param stock 주식코드
 * @param stockName 주식명
 * @param closePrice 종가
 * @param savedClosePrice 저장 당시 종가
 * @param trendFollowPrice 추세평균가
 * @param savedTrendFollowPrice 저장 당시 추세평균가
 * @param isBuy 매매여부
 * @param savedIsBuy 저장 당시 매매여부
 */
public record TrendFollowUserPageDto(
         String stock
        ,String stockName
        ,String closePrice
        ,String savedClosePrice
        ,String trendFollowPrice
        ,String savedTrendFollowPrice
        ,Boolean isBuy
        ,Boolean savedIsBuy
        ,Boolean notification
) {
}
