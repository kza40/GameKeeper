package ca.cmpt276.myapplication.model;

/*
This will be the class holding the scores, and I suspect our categorization of the points (ugly unicorn)
 */

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameConfig implements Iterable<Game>{


    private final List<Game> games = new ArrayList<>();

    private String gameTitle;
    private int poorScore;
    private int goodScore;


    public GameConfig(String gameTitle, int poorScore, int goodScore) {
        this.gameTitle = gameTitle;
        this.poorScore = poorScore;
        this.goodScore = goodScore;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
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


    @NonNull
    @Override
    public Iterator<Game> iterator() {
        return games.iterator();
    }
}
