package com.util.utilslibrary;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间操作工具类
 */
public class DateUtil {

    private static final long TIMER_MSG = 5 * 60 * 1000;

    //  private static final long TIMER_MSG = 30 * 1000;
    public static int getDateYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }


    /**
     * 得到当前日期时间的字符串。
     *
     * @param format 日期格式，如yyyy-MM-dd HH:mm:ss
     * @return 当前日期字符串
     */
    public static String currentDate(String format) {
        String currentDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            currentDate = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    /**
     * 获取当前时间的Long型
     *
     * @param format 格式
     * @return 返回long
     */
    public static long currentTime(String format) {
        String currentDate = currentDate(format);
        return dateFormat(currentDate, format).getTime();
    }

    /**
     * 将字符串转换成指定格式日期
     *
     * @param dateStr 字符串
     * @param format  格式 如果格式为null，默认格式：yyyy-MM-dd
     * @return 日期
     */
    public static Date dateFormat(String dateStr, String format) {
        return dateFormat(dateStr, format, TimeZone.getDefault());
    }

    /**
     * 将字符串转换成指定格式日期
     *
     * @param dateStr  字符串
     * @param format   格式 如果格式为null，默认格式：yyyy-MM-dd
     * @param timeZone 时区
     * @return 日期
     */
    public static Date dateFormat(String dateStr,
                                  String format,
                                  TimeZone timeZone) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(timeZone);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取milliseconds表示的日期的字符串
     *
     * @param milliseconds 日期的milliseconds
     * @param format       格式化字符串，如"yyyy-MM-dd HH:mm:ss"
     * @return String 日期的字符串表示
     */
    public static String dateFormat(long milliseconds, String format) {
        return dateFormat(milliseconds, format, TimeZone.getDefault());
    }

    /**
     * 获取milliseconds表示的日期的字符串
     *
     * @param milliseconds 日期的milliseconds
     * @param format       格式化字符串，如"yyyy-MM-dd HH:mm:ss"
     * @param timeZone     时区
     * @return String 日期的字符串表示
     */
    public static String dateFormat(long milliseconds,
                                    String format,
                                    TimeZone timeZone) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.getDefault());
            sdf.setTimeZone(timeZone);
            Date date = new Date(milliseconds);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * UTC时间转换
     *
     * @param srcTime 时间
     * @return 时间字符串
     */
    public static String dateFormatUTC(String srcTime,
                                       String before,
                                       String after) {
        if (TextUtils.isEmpty(srcTime))
            return "";
        SimpleDateFormat sdf =
                new SimpleDateFormat(before, Locale.getDefault());
        SimpleDateFormat dspFmt = new SimpleDateFormat(after,
                Locale.getDefault());
        Date resultDate;
        long resultTime;
        // 如果传入参数异常，使用本地时间
        if (null == srcTime)
            resultTime = System.currentTimeMillis();
        else {
            try { // 将输入时间字串转换为UTC时间
                sdf.setTimeZone(TimeZone.getDefault());
                resultDate = sdf.parse(srcTime);
                resultTime = resultDate.getTime();
            } catch (Exception e) { // 出现异常时，使用本地时间
                resultTime = System.currentTimeMillis();
                dspFmt.setTimeZone(TimeZone.getDefault());
                return dspFmt.format(resultTime);
            }
        }
        // 设定时区
        dspFmt.setTimeZone(TimeZone.getDefault());
        return dspFmt.format(resultTime);
    }

    /**
     * 获取当前时间所在当前周的周开始和结束时间
     *
     * @return 开始和结束时间
     */
    public static long[] timeForWeekStartEnd() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        c.setTimeInMillis(c.getTimeInMillis() / 1000 * 1000); // 去掉毫秒
        c.set(Calendar.SECOND, 0); // 去掉秒
        c.set(Calendar.MINUTE, 0); // 去掉分
        c.set(Calendar.HOUR, 0);
        // 获取当前毫秒值
        long curTime = c.getTimeInMillis();
        // 获取当天的星期数
        int week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) // 周日
            week = 7;
        long day = 24 * 60 * 60 * 1000; // 一天毫秒数
        // 计算所剩天数
        long futTime = (7 - week) * day;
        // 计算已经消耗天数
        long alrTime = (week - 1) * day;
        long[] result = new long[2];
        result[0] = curTime - alrTime;
        result[1] = curTime + futTime;
        return result;
    }

    /**
     * 获取当天的开始时间
     *
     * @return 开始和结束时间
     */
    public static long[] timeForDayStartEnd() {
        long[] result = new long[2];
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        c.setTimeInMillis(c.getTimeInMillis() / 1000 * 1000); // 去掉毫秒
        c.set(Calendar.SECOND, 0); // 去掉秒
        c.set(Calendar.MINUTE, 0); // 去掉分
        c.set(Calendar.HOUR, 0);
        // 获取当前毫秒值
        result[0] = c.getTimeInMillis();
        result[1] = result[0] + 24 * 60 * 60 * 1000;
        return result;
    }

    /**
     * 当前的年龄
     *
     * @param currentAge
     */
    public static int getCurrentAge(String currentAge) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR); //当前的年分
        int month = c.get(Calendar.MONTH); //当前的年分
        String[] ageArr = currentAge.split("-");
        int age = year - Integer.valueOf(ageArr[0]);
        if (Integer.valueOf(ageArr[1]) > month) {
            age += 1;
            return age;
        } else {
            return age;
        }
    }

    /**
     * 吧一个Date转换成Str
     *
     * @param date
     * @return
     */
    //把日期转为字符串
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    public static boolean isTimer(long time1, long time2) {
        // long time1 = date1.getTime();
        // long time2 = date2.getTime();
        long delta = time1 - time2;
        if (delta < 0) {
            delta = -delta;
        }
        return delta < TIMER_MSG;
    }

    // 2017-07-19 15:07:39
    public static String getAdvCommentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 时间差
     *
     * @param strDate 开始时间
     * @return long
     */
    public static long getTimeDifference(long strDate) {
        Log.e("time", ((System.currentTimeMillis() - strDate) / 1000) + "");
        if (((System.currentTimeMillis() - strDate) / 1000) < 60)
            return (60 * 1000) - (System.currentTimeMillis() - strDate);
        else
            return -1;
    }

    /**
     * 将date 转换为自己日期格式
     *
     * @param date       date
     * @param dateFormat yyyy-MM-dd HH:mm
     * @return
     */
    public static String getCurrentTimeMillis(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

}
