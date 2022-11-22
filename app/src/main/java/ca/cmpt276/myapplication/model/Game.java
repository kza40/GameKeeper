package ca.cmpt276.myapplication.model;

import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game class contains the details of a single game: the date it was played, group score,
 * number of players, the achievement earned, and the scale factor (based on chosen difficulty).
 */

public class Game {
    private String achievementEarned;
    private int numOfPlayers;
    private int groupScore;
    private float scaleFactor;
    private String[] playerScores;
    private String datePlayed;

    public Game(String achievementEarned, int numOfPlayers, int groupScore, int poorScore, int greatScore, float scaleFactor,String[] playerScores)
    {
        this.achievementEarned = achievementEarned;
        this.numOfPlayers = numOfPlayers;
        this.groupScore = groupScore;
        this.scaleFactor = scaleFactor;
        this.playerScores=playerScores;
        setDate();
    }

    private void setDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd");
        LocalDateTime now = LocalDateTime.now();
        datePlayed = now.format(format);
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public String getPlayerScoresAtIndex(int position)
    {
        return playerScores[position];
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

    public void setAchievementEarned(String achievementEarned) {
        this.achievementEarned = achievementEarned;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setGroupScore(int groupScore) {
        this.groupScore = groupScore;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void setPlayerScores(String[] playerScores) {
        this.playerScores = playerScores;
    }

    public void setDatePlayed(String datePlayed) {
        this.datePlayed = datePlayed;
    }
}
