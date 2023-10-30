package com.server.quant_bot.comm.utill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtill {

    private DateUtill(){};

    public static LocalDate getStrToLocalDate(String date, String pattern){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * yyyMMdd로 오늘 날짜를 준다.
     * @return
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
