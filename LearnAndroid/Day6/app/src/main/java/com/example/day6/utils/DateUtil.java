package com.example.day6.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
    }

    private static DateUtil instance;

    public static synchronized DateUtil getInstance() {
        if (instance == null) {
            instance = new DateUtil();
        }
        return instance;
    }

    /**
     * 获取前一天的日期
     *
     * @param dateString 当前日期字符串，格式为 "yyyyMMdd"
     * @return 前一天的日期字符串
     * @throws ParseException 解析异常
     */
    public String getPreviousDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);

        return sdf.format(calendar.getTime());
    }

    /**
     * 获取后一天的日期
     *
     * @param dateString 当前日期字符串，格式为 "yyyyMMdd"
     * @return 后一天的日期字符串
     * @throws ParseException 解析异常
     */
    public String getNextDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);

        return sdf.format(calendar.getTime());
    }

    /**
     * 计算两个日期之间的天数差距
     *
     * @param startDateString 开始日期字符串，格式为 "yyyyMMdd"
     * @param endDateString   结束日期字符串，格式为 "yyyyMMdd"
     * @return 日期差距天数
     * @throws ParseException 解析异常
     */
    public int daysBetween(String startDateString, String endDateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        long diffMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        return (int) (diffMillis / (1000 * 60 * 60 * 24));
    }
}
