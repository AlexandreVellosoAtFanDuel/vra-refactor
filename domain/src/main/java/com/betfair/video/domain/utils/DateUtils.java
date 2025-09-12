package com.betfair.video.domain.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date shiftDateByField(Date date, int field, int amount) {
        Calendar calendar = getCalendarInstance(date);
        calendar.add(field, amount);
        return new Date(calendar.getTimeInMillis());
    }

    private static Calendar getCalendarInstance(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static Date getCurrentDate() {
        return new Date();
    }
}
