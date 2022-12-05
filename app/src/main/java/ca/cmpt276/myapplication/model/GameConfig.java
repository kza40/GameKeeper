package ca.cmpt276.myapplication.model;

/**
 * The GameConfig class stores the details of a single game configuration.
 * It contains the: title, poor and great scores, as well as a list of games.
 */

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameConfig implements Iterable<Game>{
    private final List<Game> games = new ArrayList<>();

    private String configTitle;
    private int poorScore;
    private int goodScore;
    private String photoFileName;

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

    public void addGame(Game game) { games.add(game); }

    public boolean isEmpty(){ return games.isEmpty(); }

    public String getPhotoFileName() {
        return photoFileName;
    }

    @NonNull
    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }
}
