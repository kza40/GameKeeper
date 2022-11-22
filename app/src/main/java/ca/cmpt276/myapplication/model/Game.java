package ca.cmpt276.myapplication.model;

import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game class contains the details of a single game: the date it was played, group score,
 * number of players, the achievement earned, and the scale factor (based on chosen difficulty).
 */

public class Game {
    private int numOfPlayers;
    private int groupScore;
    private String datePlayed;
    private String achievementEarned;
    private float scaleFactor;
    private String[] playerScores;

    public Game(String[] names, int numOfPlayers, int groupScore, int poorScore, int greatScore, float scaleFactor,String[] playerScores) {
        this.numOfPlayers = numOfPlayers;
        this.groupScore = groupScore;
        this.scaleFactor = scaleFactor;
        this.playerScores=playerScores;
        setDate();
        setAchievementEarned(names, poorScore, greatScore);
    }

    private void setDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd");
        LocalDateTime now = LocalDateTime.now();
        datePlayed = now.format(format);
    }

    private void setAchievementEarned(String[] names, int poorScore, int greatScore) {
        achievementEarned = AchievementCalculator
                            .getAchievementEarned(names, numOfPlayers, poorScore, greatScore, groupScore, scaleFactor);
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public String getDatePlayed() {
        return datePlayed;
    }

    public String getAchievementEarned() {
        return achievementEarned;
    }

    public int getGroupScore() {
        return groupScore;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }
}
