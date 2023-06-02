package com.pop_up_balloon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class which provides utility to handle dates
 */
public class DateUtils {
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());

    /**
     * Convert date object to String using SimpleDateFormat
     *
     * @param date Date which needs to be converted to String
     * @return String converted from Date
     */
    public String convertDateToString(Date date) {
        return sdf.format(date);
    }

    /**
     * Convert string to Date using SimpleDateFormat
     *
     * @param date String which needs to be converted to Date
     * @return Date parsed from String
     */
    public Date convertStringToDate(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validate Date
     *
     * @param date which needs to be validated
     * @return True if Valid, otherwise false
     */
    public boolean isValid(String date) {
        try {
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
