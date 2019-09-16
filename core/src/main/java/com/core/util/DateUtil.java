package com.core.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil extends DateUtils {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 获取当前时间时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     */
    public static String getTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * 获取给定时间字符串
     */
    public static String getTimeStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 获取给定时间字符串
     */
    public static String getTimeStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前时间字符串
     */
    public static Date getDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toInstant());
    }

    /**
     * 获取当前时间字符串
     */
    public static String getTimeStr(String DATE_FORMAT) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * 时间戳转字符串
     */
    public static String getTimeStr(long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date(seconds));
    }

    /**
     * 字符串转时间戳
     */
    public static long getTimestamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Date str2Date(String datestr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            Date date = format.parse(datestr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据日期格式,字符串转date
     */
    public static Date str2Date(String datestr, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date date = format.parse(datestr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 求开始截至日期之间的天数差.
     *
     * @param smdate 开始日期
     * @param bdate  截至日期
     * @return 返回相差天数
     */
    public static Integer getDays(Date smdate, Date bdate) {
        if (smdate == null || bdate == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个时间戳之间相差多少秒
     */
    public static long getDistanceTime(long time1, long time2) {
        long day;
        long hour;
        long min;
        long sec;
        long diff;
        diff = time1 - time2;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return sec;
    }

    /**
     * 获取过去第几天的日期
     */
    public static long getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        return calendar.getTime().getTime();
    }

    /**
     * 获取未来 第 past 天的日期
     */
    public static long getFeatureDate(int feature) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + feature);
        return calendar.getTime().getTime();
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getBetDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                //days.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取startdate未来或过去几天的日期
     *
     * @param startDate yyyy-MM-dd
     * @param count     +7:未来7天 -7:过去7天
     * @return
     */
    public static String getTargetDate(String startDate, int count) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(startDate, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        //包含开始日期,结束日期,故count-1
        calendar.add(Calendar.DATE, count-1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取日期之间的所有周五，六日期
     *
     * @param list
     * @return
     */
    public static List<Date> getWeekendDate(List<Date> list){
        Calendar calendar= Calendar.getInstance();
        List<Date> weekendDate=new ArrayList<Date>();
        for (int a=0;a<list.size();a++){
            calendar.setTime(list.get(a));
            if (calendar.get(Calendar.DAY_OF_WEEK)== Calendar.FRIDAY||calendar.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
                weekendDate.add(list.get(a));
            }
        }
        return weekendDate;
    }


    public static void main(String[] args) {

        System.out.println(getTimeStr(getFeatureDate(7)));
    }

}
