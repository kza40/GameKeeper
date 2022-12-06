package ca.cmpt276.myapplication.model;

import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game class contains the details of a single game: the date it was played, group score,
 * number of players, the achievement earned, and the scale factor (based on chosen difficulty).
 */

public class Game {
    private int achievementPos;
    private int numOfPlayers;
    private int groupScore;
    private float scaleFactor;
    private String[] playerScores;
    private String datePlayed;
    private String photoFileName;

    public Game(int achievementPos, int numOfPlayers, int groupScore, float scaleFactor,String[] playerScores,String photoFileName)
    {
        this.achievementPos = achievementPos;
        this.numOfPlayers = numOfPlayers;
        this.groupScore = groupScore;
        this.scaleFactor = scaleFactor;
        this.playerScores=playerScores;
        this.photoFileName=photoFileName;
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

    public int getAchievementPos() {
        return achievementPos;
    }

    public int getGroupScore() {
        return groupScore;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setAchievementPos(int achievementPos) {
        this.achievementPos = achievementPos;
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

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }
}
