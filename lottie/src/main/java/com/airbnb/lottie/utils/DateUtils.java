package com.airbnb.lottie.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_YYYY = "yyyy";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String FORMAT_MM_SS = "mm:ss";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY2MM2DD = "yyyy.MM.dd";
    public static final String FORMAT_YYYY2MM2DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_MMCDD_HH_MM = "MM月dd日 HH:mm";
    public static final String FORMAT_MMCDD = "MM月dd日";
    public static final String FORMAT_YYYYCMMCDD = "yyyy年MM月dd日";

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;
    

    public static boolean isNowMoreThanOtherTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int today = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DATE, today);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long otherTime = c.getTime().getTime();
        long nowTime = System.currentTimeMillis();
        return nowTime >= otherTime;
    }

    /**
     * 获取某年某月有多少天
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间相差的天数
     *
     * @return time1 - time2相差的天数
     */
    public static int getDayOffset(long time1, long time2) {
        // 将小的时间置为当天的0点
        long offsetTime;
        if (time1 > time2) {
            offsetTime = time1 - getDayStartTime(getCalendar(time2)).getTimeInMillis();
        } else {
            offsetTime = getDayStartTime(getCalendar(time1)).getTimeInMillis() - time2;
        }
        return (int) (offsetTime / ONE_DAY);
    }

    public static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    /**
     * 获取日期是当月的那天
     *
     * @param time
     * @return
     */
    public static int getCalendarDayOfMouth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getCalendarMouthOfYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int mouth = calendar.get(Calendar.MONTH);
        return mouth;
    }


    public static Calendar getDayStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String getDurationInString(long time) {
        String durStr = "";
        if (time == 0) {
            return "0秒";
        }
        time = time / 1000;
        long hour = time / (60 * 60);
        time = time - (60 * 60) * hour;
        long min = time / 60;
        time = time - 60 * min;
        long sec = time;
        if (hour != 0) {
            durStr = hour + "时" + min + "分" + sec + "秒";
        } else if (min != 0) {
            durStr = min + "分" + sec + "秒";
        } else {
            durStr = sec + "秒";
        }
        return durStr;
    }

    /**
     * 获取当前时间是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }


    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convertToLong(String date, String format) {
        try {
            if (!TextUtils.isEmpty(date)) {
                if (TextUtils.isEmpty(format)) {
                    format = TIME_FORMAT;
                }
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                return formatter.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToString(long time, String format) {
        if (time > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";

    }

}