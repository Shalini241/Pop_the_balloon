package com.pop_up_balloon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.pop_up_balloon.objects.Player;
import com.pop_up_balloon.utils.DateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Class to provide player data to UI and to save data to file
 */
public class DataProvider {
    private static final String TAG = "DataProvider";
    private final Context context;
    public DataProvider(Context context) {
        this.context = context;
    }
    private final String FILE_NAME = "scoreboard.tsv";

    @SuppressLint("NewApi")
    /**
     * Update list of players in file by re-writing the whole file
     *
     * @param players updated list of players
     */
    public void updateCache(List<Player> players) {

        players.sort(playerComparator);
        File file = null;
        try {
            File directory = new File(context.getCacheDir(), "");

            /* Check whether directory exists or not, if not then create one */
            if (!directory.exists())
                directory.mkdirs();

            file = new File(directory + "/" + FILE_NAME);
            /*
             * Check if TSV file exists, if it does then delete it, and
             * create new file as we want to re-write the whole file every time a list updated
             */
            if (file.exists()) file.delete();
            file.createNewFile();

            if (players.size() > 0) {
                FileWriter fw = new FileWriter(file);
                String result = "";
                StringBuilder sb = new StringBuilder();

                for (int i=0;i<20 && i<players.size();i++) {
                    Player player = players.get(i);
                    sb.append(player.toString()).append("\n");
                }
                result = sb.deleteCharAt(sb.length() - 1).toString();

                fw.write(result);
                fw.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Err: " + e);
        }
    }

    /**
     * Parse the TSV file and extract all the players stored in cache file
     *
     * @return List of players sorted by Score and then Date - playerComparator
     */
    @SuppressLint("NewApi")
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        BufferedReader br = null;
        try {
            String currentLine;
            br = new BufferedReader(new FileReader(context.getCacheDir() + "/" + FILE_NAME));

            while ((currentLine = br.readLine()) != null) {
                String[] tabLine = currentLine.split("\t");

                /* Parse string data to model */
                String name = tabLine[0];
                long score = Long.parseLong(tabLine[1]);
                Date date = new DateUtils().convertStringToDate(tabLine[2]);

                /* Create model class and add to List */
                players.add(new Player(name, score, date));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return players;
    }

    /**
     * Comparator to sort the players list first by descending order of their score and then, descending order of the date
     */
    @SuppressLint("NewApi")
    private final Comparator<Player> playerComparator = Comparator
            /* Sort by Score */
            .comparing(Player::getScore, Comparator.reverseOrder())
            /* Sort by Date (most recent at top) */
            .thenComparing(Player::getDate, Comparator.reverseOrder());
}
