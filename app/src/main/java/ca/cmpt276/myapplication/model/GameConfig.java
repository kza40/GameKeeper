package ca.cmpt276.myapplication.model;

/**
 * The GameConfig class stores the details of a single game configuration.
 * It contains the: title, poor and great scores, as well as a list of games.
 */

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.cmpt276.myapplication.PreviewAchievements;
import ca.cmpt276.myapplication.ui_features.AchievementManager;

public class GameConfig implements Iterable<Game>{
    private final List<Game> games = new ArrayList<>();

    private String configTitle;
    private int poorScore;
    private int goodScore;
    private String photoFileName;
    private int[] achievementPosCounter = new int[AchievementManager.NUMBER_OF_ACHIEVEMENT_POS];

    public GameConfig(String configTitle, int poorScore, int goodScore, String photoFileName) {
        this.configTitle = configTitle;
        this.poorScore = poorScore;
        this.goodScore = goodScore;
        this.photoFileName = photoFileName;
    }

    public List<Game> getGames() {
        return games;
    }

    public String getConfigTitle() {
        return configTitle;
    }

    public void setConfigTitle(String configTitle) {
        this.configTitle = configTitle;
    }

    public Game getGameAtIndex(int configPos)
    {
        return games.get(configPos);
    }

    public int getPoorScore() {
        return poorScore;
    }

    public void setPoorScore(int poorScore) {
        this.poorScore = poorScore;
    }

    public int getGoodScore() {
        return goodScore;
    }

    public void setGoodScore(int goodScore) {
        this.goodScore = goodScore;
    }

    public void addGame(Game game) {
        games.add(game);
        achievementPosCounter[game.getAchievementPos()]++;
    }
    public int[] getAchievementPosCounter() { return achievementPosCounter; }
    public boolean isEmpty(){ return games.isEmpty(); }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    @NonNull
    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }
}
