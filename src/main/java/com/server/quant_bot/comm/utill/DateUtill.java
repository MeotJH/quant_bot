package com.server.quant_bot.comm.utill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * static class to get date data - Meot -
 */
public class DateUtill {

    private DateUtill(){};

    public static LocalDate getStrToLocalDate(String date, String pattern){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * return today Date format to yyyMMdd.
     * - Meot -
     * @return yyyyMMdd String Data
     */
    public static String getToday(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 원하는 패턴대로 날짜를 준다.
     * @param pattern
     * @return
     */
    public static String getToday(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}
