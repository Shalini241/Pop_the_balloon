package com.pop_up_balloon.utils;

/**
 * Class to validate player data (Name and Score)
 */
public class Validator {
    /**
     * Validate name
     *
     * @param name of the player
     * @return Error String
     */
    public static String validateName(String name) {
        /* Validate name - Must not be empty */
        if (name.isEmpty()) return "Please enter name";
            /* Validate name - Must be less than 25 chars */
        else if (name.length() > 25) return "Max length can be 25";
        return null;
    }

    /**
     * Validate Date
     *
     * @param date
     * @return Error String
     */
    public static String validateDate(String date) {
        /* Validate score - Must not be empty */
        if (date.isEmpty()) return "Please enter date";
            /* Validate score - Must be greater than 0 */
        else if (!new DateUtils().isValid(date)) return "Please enter date/time in defined format";
        return null;
    }
}