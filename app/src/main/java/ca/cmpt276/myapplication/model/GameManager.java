package ca.cmpt276.myapplication.model;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
This will be the class that manages the inputted games
 */
public class GameManager implements Iterable<GameConfig> {
    private final List<GameConfig> gameConfigs = new ArrayList<>();
    private static GameManager instance;


    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public List<GameConfig> getGameConfigs() {
        return gameConfigs;
    }

    public void addGame(GameConfig gameConfig){ gameConfigs.add(gameConfig); }


    public boolean isEmpty() { return gameConfigs.isEmpty(); }

    @NonNull
    @Override
    public Iterator<GameConfig> iterator() { return gameConfigs.iterator(); }

}
