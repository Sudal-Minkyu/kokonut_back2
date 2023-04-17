package com.app.kokonut.history.service;

import java.sql.Date;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class H2CustomAlias {

    public static String date_format(Date date, String mysqlFormatPattern) {
        if (date == null) return null;
        String dateFormatPattern = mysqlFormatPattern
            .replace("%Y", "yyyy")
            .replace("%m", "MM")
            .replace("%d", "dd");
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate().format(DateTimeFormatter.ofPattern(dateFormatPattern));
    }

    public static Date date_sub(Date date, int day) {

        // 현재 LocalDate에서 3일후인 날짜계산
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -day);

        return new Date(c.getTimeInMillis());
    }

}