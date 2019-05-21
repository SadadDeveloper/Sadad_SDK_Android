package com.sdkdemo.utils;

import android.text.format.DateUtils;

import com.sdkdemo.base.SdkApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This Class contains all date formatter methods like
 * <p/>
 * ================================================================================
 * convert DateToString ,StringToDate,
 * find Remaing Days , Hours, Minutes, Seconds between two dates.
 * get date from given timestamp
 * get timestamp from given Date
 * formatting Date
 * <p/>
 * Ex. If you convert StringToDate use stringToDateConversion("2/12/15","dd/mm/yy")
 * =================================================================================
 * Created by Yama on 2/12/15.
 */
public class DateFormatConversion {

    /**
     * Changed by yama - Starting
     * Date: 24-Dec-2018.
     * Reason: Now onward get Qatar time format so changed the date format
     **/
    public static final String yyyy_MM_dd_T_HH_mm_ss_SSS_Z = "yyyy-MM-dd HH:mm:ss";
//    public static final String yyyy_MM_dd_T_HH_mm_ss_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    /**
     * Changed by yama - Ending
     * Date: 24-Dec-2018.
     * Reason: Now onward get Qatar time format so changed the date format
     **/
    public static final String dd_MMM_yyyy_hh_mm_a = "dd MMM yyyy hh:mm a";
    public static final String dd_MMM_yyyy = "dd MMM yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MMMM_DD_YYYY_HH_MM_A = "MMMM dd,yyyy hh.mm a";

    public static String timestampToDateStrConversion(long timestamp, String DateFormatString) {
        Date convertedDate = getDateFromTimestamp(timestamp);
        return dateToStringConversion(convertedDate, DateFormatString);
    }

    /**
     * Convert String to Date Object
     *
     * @param dateString
     * @param DateFormatString (gives the format of String in which format you pass the string
     *                         ex. StringToDateConversion("02/12/2015","dd//MM/yyyy")
     * @return Date object
     * <p/>
     * Date object always gives standard Data Format
     */
    public static Date stringToDateConversion(String dateString, String DateFormatString) {
        Date convertedDate = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatString);
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    /**
     * Convert String to Date Object
     *
     * @param dateString
     * @param currentFormat  (gives the format of String in which format you pass the string
     *                       ex. StringToDateConversion("02/12/2015","dd//MM/yyyy")
     * @param requiredFormat (In witch format you want string)
     * @return Date object
     * <p/>
     * Date object always gives standard Data Format
     */
    public static String stringToStringConversion(String dateString, String currentFormat, String requiredFormat) {
        String convertedDateStr = "";

        if (dateString != null && !dateString.isEmpty() && currentFormat != null && !currentFormat.isEmpty() && requiredFormat != null && !requiredFormat.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat);
                Date convertedDate = dateFormat.parse(dateString);
                convertedDateStr = dateToStringConversion(convertedDate, requiredFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return convertedDateStr;
    }

    public static String oneStringToOtherString(String currentFormat, String newFormat, String inputString) {

        String reformattedStr = "";

        SimpleDateFormat fromUser = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        SimpleDateFormat myFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);

        try {

            reformattedStr = myFormat.format(fromUser.parse(inputString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reformattedStr;
    }

    /**
     * Convert Date to String with desired format
     *
     * @param date
     * @param DateFormatString (In witch format you want string)
     * @return
     */

    public static String dateToStringConversion(Date date, String DateFormatString) {
        String dateString = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatString);
            dateString = dateFormat.format(date);

            dateString = getDateInEnglish(dateString, DateFormatString);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return dateString;
    }

    public enum RemainingType {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
    }

    /**
     * Get difference between two date in days, hours, minutes and seconds
     *
     * @param startDate     - give start date
     * @param endDate       - give end date
     * @param remainingType - specify type(days,hours, minutes and seconds)to find difference between two dates.
     * @return
     */

    public static long getDateDifferenceInX(Date startDate, Date endDate, RemainingType remainingType) {

        long remaining = 0;
        long diffInMilliSec = startDate.getTime() - endDate.getTime();

        if (remainingType == RemainingType.SECONDS) {
            remaining = diffInMilliSec / DateUtils.SECOND_IN_MILLIS;
        } else if (remainingType == RemainingType.MINUTES) {
            remaining = diffInMilliSec / DateUtils.MINUTE_IN_MILLIS;
        } else if (remainingType == RemainingType.HOURS) {
            remaining = diffInMilliSec / DateUtils.HOUR_IN_MILLIS;
        } else if (remainingType == RemainingType.DAYS) {
            remaining = diffInMilliSec / DateUtils.DAY_IN_MILLIS;
        }

        return remaining;
    }

    /**
     * Get Date from given Timestamp
     *
     * @param timestamp - provide timestamp for gettimg date of given timestamp
     * @return
     */

    public static Date getDateFromTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeZone(tz);
        calendar.setTimeInMillis(timestamp);

        return calendar.getTime();
    }

    /**
     * Get Timestamp from given Date
     *
     * @param date - provide date for getting timestamp of given date
     * @return
     */

    public static long getTimestampFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.getTimeInMillis();
    }

    /**
     * Pass the Date and with desired format. It return string with given format
     *
     * @param date             -
     * @param dateFormatString - in which format you want to get date.
     * @return
     */

    public static String changeDateFormat(Date date, String dateFormatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);

        return sdf.format(date);
    }

    /**
     * Get the Date before and After X Days
     *
     * @param days
     * @return
     */

    public static Date getDateBeforeAfterXDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);

        return calendar.getTime();
    }

    public static String getDateInEnglish(String inputDate, String dateFormat) {

        String finalDate = "";

        if (LocaleUtils.getLocale(SdkApp.getInstance()).equalsIgnoreCase("en")) {
            finalDate = inputDate;
        } else {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                Date date = sdf.parse(inputDate);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                finalDate = finalDateFormat.format(date);

//                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//                Date date = sdf.parse(inputDate);
//                SimpleDateFormat finalDateFormat = new SimpleDateFormat(dateFormat);
//                finalDate = finalDateFormat.format(date);
//
//                SimpleDateFormat newDateFormat = new SimpleDateFormat(finalDate);
//                Date convertedDate = newDateFormat.parse(finalDate);
//
//                SimpleDateFormat newDateFormat1 = new SimpleDateFormat(dateFormat);
//                finalDate = newDateFormat1.format(convertedDate);

//                finalDate = stringToStringConversion(finalDate, finalDate, dateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return finalDate;
    }
}

