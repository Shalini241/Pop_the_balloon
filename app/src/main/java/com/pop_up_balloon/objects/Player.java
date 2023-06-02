package com.pop_up_balloon.objects;

import androidx.annotation.NonNull;

import com.pop_up_balloon.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Data class to represent player information
 */
public class Player implements Serializable {
    private String name;
    private long score;
    private Date date;

    /**
     * Creates a player with the following information
     *
     * @param name Name of the Player
     * @param score Score of the player
     * @param date Date when the score is created
     */
    public Player(String name, long score, Date date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Overwrite .toString() to return parameters separated by tab (\t)
     * @return Tab formatted string
     */
    @NonNull
    @Override
    public String toString() {
        return name + "\t" + score + "\t" + new DateUtils().convertDateToString(date);
    }
}