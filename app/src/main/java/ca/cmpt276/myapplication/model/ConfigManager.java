package ca.cmpt276.myapplication.model;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ConfigManager is a singleton class. It contains all game configurations set up by the user.
 */

public class ConfigManager implements Iterable<GameConfig> {
    private static ConfigManager instance;
    private final List<GameConfig> gameConfigs;

    public static ConfigManager getInstance(){
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private ConfigManager() {
        gameConfigs = new ArrayList<>();
    }

    public List<GameConfig> getGameConfigs() {
        return gameConfigs;
    }

    public GameConfig getGameConfigAtIndex(int configPos)
    {
        return gameConfigs.get(configPos);
    }
    public int size() { return gameConfigs.size(); }
    public void addGame(GameConfig gameConfig) {
        gameConfigs.add(gameConfig);
    }

    public boolean isEmpty() {
        return gameConfigs.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<GameConfig> iterator() {
        return gameConfigs.iterator();
    }

    public void removeConfigAtIndex(int position) {
        gameConfigs.remove(position);
    }
}
