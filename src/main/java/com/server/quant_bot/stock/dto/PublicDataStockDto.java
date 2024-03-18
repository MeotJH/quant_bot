package com.server.quant_bot.stock.dto;

import lombok.Getter;
import lombok.Setter;
/*
POJO
 */
@Setter
@Getter
public class PublicDataStockDto {

    /**
     * 기준일자
     */
    private String basDt;

    //단축코드
    private String srtnCd;

    //ISIN코드 : 국제 채권 식별 번호, 유가증권(채권)의 국제인증 고유번호
    private String isinCd;

    // 종목명 : 종목의 명칭
    private String itmsNm;

    // 시장구분 : 주식의 시장 구분
    private String mrktCtg;

    // 종가 : 정규시장의 매매시간 종료시까지 형성되는 최종가격
    private String clpr;

    // 대비 : 전일 대비 등락
    private String vs;

    // 등락률 : 전일 대비 등락에 따른 비율
    private String fltRt;

    // 시가 : 정규시장의 매매시간 개시 후 형성되는 최초가격
    private String mkp;

    // 고가 : 하루 중 가격의 최고치
    private String hipr;

    // 저가 : 하루 중 가격의 최저치
    private String lopr;

    // 거래량 : 체결수량의 누적 합계
    private String trqu;

    // 거래대금 : 거래건 별 체결가격 * 체결수량의 누적 합계
    private String trPrc;

    // 상장주식수 : 종목의 상장주식수
    private String lstgStCnt;

    // 시가총액 : 종가 * 상장주식수
    private String mrktTotAmt;
}
