package com.server.quant_bot.comm.utill;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtill {

    private DateUtill(){};

    public static LocalDate getStrToLocalDate(String date, String pattern){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
