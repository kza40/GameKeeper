package ca.cmpt276.myapplication.model;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
This will be the class that manages the inputted games
 */
public class GameManager implements Iterable<Game> {
    private final List<Game> games = new ArrayList<>();
    private static GameManager instance;


    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void addGame(Game game){ games.add(game); }

    public boolean isEmpty() { return games.isEmpty(); }

    @NonNull
    @Override
    public Iterator<Game> iterator() { return games.iterator(); }

}
