package ca.cmpt276.myapplication.model;

/*
This will be the class holding the scores, and I suspect our categorization of the points (ugly unicorn)
 */

public class Game {



    private String gameTitle;
    private int poorScore;
    private int goodScore;


    public Game(String gameTitle, int poorScore, int goodScore) {
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


}
