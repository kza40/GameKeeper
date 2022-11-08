package ca.cmpt276.myapplication.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Game class contains the details of a single game: the date it was played, group score,
 * number of players, and the achievement earned.
 */

public class Game {
    private int numOfPlayers;
    private int groupScore;
    private String datePlayed;
    private String achievementEarned;

    public Game(String[] names, int numOfPlayers, int groupScore, int poorScore, int greatScore) {
        this.numOfPlayers = numOfPlayers;
        this.groupScore = groupScore;
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
                            .getAchievementEarned(names, numOfPlayers, poorScore, greatScore, groupScore);
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
}
