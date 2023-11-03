package com.server.quant_bot.comm.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    /**
     * yyyyMMdd로 날짜를 넣으면 요일을 알려준다.
     * @param yyyyMMdd
     * @return
     */
    public static String getDayOfWeek(String yyyyMMdd){
        String dayOfWeek = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
            Date date = dateFormat.parse(yyyyMMdd);
            dayOfWeek = dayOfWeekFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dayOfWeek;
    }

    public static String getMinusDay(String yyyyMMdd, int minus){
        LocalDate date = LocalDate.parse(yyyyMMdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return date.minusDays(minus).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
