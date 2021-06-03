package com.sadadsdk.utils;

import android.text.TextUtils;

import java.util.Calendar;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class DateValidator {

    public static final int MAXIMUM_VALID_YEAR_DIFFERENCE = 20;
    private static final DateValidator INSTANCE = new DateValidator(Calendar.getInstance());
    private final Calendar mCalendar;

    protected DateValidator(Calendar calendar) {
        this.mCalendar = calendar;
    }

    public static boolean isValid(String month, String year) {
        return INSTANCE.isValidHelper(month, year);
    }

    protected boolean isValidHelper(String monthString, String yearString) {
        if (TextUtils.isEmpty(monthString)) {
            return false;
        } else if (TextUtils.isEmpty(yearString)) {
            return false;
        } else if (TextUtils.isDigitsOnly(monthString) && TextUtils.isDigitsOnly(yearString)) {
            int month = Integer.parseInt(monthString);
            if (month >= 1 && month <= 12) {
                int currentYear = this.getCurrentTwoDigitYear();
                int yearLength = yearString.length();
                int year;
                if (yearLength == 2) {
                    year = Integer.parseInt(yearString);
                } else {
                    if (yearLength != 4) {
                        return false;
                    }

                    year = Integer.parseInt(yearString.substring(2));
                }

                if (year == currentYear && month < this.getCurrentMonth()) {
                    return false;
                } else {
                    if (year < currentYear) {
                        int adjustedYear = year + 100;
                        if (adjustedYear - currentYear > 20) {
                            return false;
                        }
                    }

                    return year <= currentYear + 20;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getCurrentMonth() {
        return this.mCalendar.get(2) + 1;
    }

    private int getCurrentTwoDigitYear() {
        return this.mCalendar.get(1) % 100;
    }
}
