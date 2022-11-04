package ca.cmpt276.myapplication.model;

public class Game {
    private int numOfPlayers;
    private int score;


    public Game(int numOfPlayers, int score) {
        this.numOfPlayers = numOfPlayers;
        this.score = score;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
