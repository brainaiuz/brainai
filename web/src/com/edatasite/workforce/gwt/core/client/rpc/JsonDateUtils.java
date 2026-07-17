package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.Date;

/**
 * Created by Virus on 7/21/14.
 */
public class JsonDateUtils {

    public static String setDate(Date date) {
        Integer offset = date.getTimezoneOffset() * 60 * 1000;
        date = new Date(date.getTime() + offset);
        return (1900 + date.getYear()) + "-" + (date.getMonth()) + "-" + date.getDate()//date.getMonth() + 1 -- because month starts from 0(0 is January)
                + "  " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }

    public static String setNonConvertableDate(Date date) {
        return (1900 + date.getYear()) + "-" + (date.getMonth()) + "-" + date.getDate()
                + "  " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }

    public static Date getDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] dateTime = date.split(" {2}");
        String[] yearMonthDate = dateTime[0].split("-");
        String[] hourMinuteSecond = dateTime[1].split(":");
        int year = Integer.valueOf(yearMonthDate[0]);
        int month = Integer.valueOf(yearMonthDate[1]);
        int day = Integer.valueOf(yearMonthDate[2]);
        int hour = Integer.valueOf(hourMinuteSecond[0]);
        int minute = Integer.valueOf(hourMinuteSecond[1]);
        int second = Integer.valueOf(hourMinuteSecond[2]);
        Date date2 = new Date(year - 1900, month, day, hour, minute, second);
        return new Date(date2.getTime() - (long) date2.getTimezoneOffset() * 60 * 1000);
    }

    public static Date getNonConvertableDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] dateTime = date.split(" {2}");
        String[] yearMonthDate = dateTime[0].split("-");
        String[] hourMinuteSecond = dateTime[1].split(":");
        int year = Integer.valueOf(yearMonthDate[0]);
        int month = Integer.valueOf(yearMonthDate[1]);
        int day = Integer.valueOf(yearMonthDate[2]);
        int hour = Integer.valueOf(hourMinuteSecond[0]);
        int minute = Integer.valueOf(hourMinuteSecond[1]);
        int second = Integer.valueOf(hourMinuteSecond[2]);
        return new Date(year - 1900, month, day, hour, minute, second);
    }

}
