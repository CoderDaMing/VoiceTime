package com.ming.voicetime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 时间日期相关工具类
 */
public class TimeDateUtil {
//    public static final String ymdhms = "yyyy-MM-dd HH:mm:ss";
//    public static final String ymdhm = "yyyy-MM-dd HH:mm";
    public static final String ymd = "yyyy-MM-dd";
//    public static final String ymds = "yyyy年MM月dd日";
    public static final String hms = "HH:mm:ss";
    public static final String hm = "HH:mm";
    public static final String ss = "ss";

    public static final long ONE_MINTER = 60000;

    //日期时间格式转换，包括String、Date、Long。Date是String和Long的桥梁。

//    /**
//     * Date转String
//     *
//     * @param data
//     * @param formatType
//     * @return
//     */
//    public static String date2String(Date data, String formatType) {
//        return new SimpleDateFormat(formatType, Locale.getDefault()).format(data);
//    }
//
//
//    /**
//     * String转Date
//     *
//     * @param strTime
//     * @param formatType
//     * @return
//     * @throws ParseException
//     */
//    public static Date string2Date(String strTime, String formatType) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat(formatType, Locale.getDefault());
//        Date date = null;
//        date = formatter.parse(strTime);
//        return date;
//    }
//
//
//    /**
//     * Long转Date
//     *
//     * @param currentTime
//     * @param formatType
//     * @return
//     * @throws ParseException
//     */
//    public static Date long2Date(long currentTime, String formatType) throws ParseException {
//        Date date = new Date(currentTime);
//        return date;
//    }


//    /**
//     * Date转Long
//     *
//     * @param date
//     * @return
//     */
//    public static long date2Long(Date date) {
//        return date.getTime();
//    }

    /**
     * Long转String
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
     *
     * @param millis
     * @param format
     * @return 时间字符串
     */
    public static String long2String(long millis, String format) {
        SimpleDateFormat sdr = new SimpleDateFormat(format, Locale.getDefault());
        return sdr.format(new Date(millis));
    }

//    /**
//     * String转Long
//     *
//     * @param strTime
//     * @param formatType
//     * @return
//     * @throws ParseException
//     */
//    public static long stringToLong(String strTime, String formatType) throws ParseException {
//        Date date = string2Date(strTime, formatType); // String类型转成date类型
//        if (date == null) {
//            return 0;
//        } else {
//            long currentTime = date2Long(date); // date类型转成long类型
//            return currentTime;
//        }
//    }
}
