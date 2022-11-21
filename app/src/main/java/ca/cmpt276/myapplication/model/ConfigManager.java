package ca.cmpt276.myapplication.model;


import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.cmpt276.myapplication.ThemeSetting;

/**
 * ConfigManager is a singleton class. It contains all game configurations set up by the user.
 */

public class ConfigManager implements Iterable<GameConfig> {
    private static ConfigManager instance;
    private final List<GameConfig> gameConfigs;
    private String theme;

    public static ConfigManager getInstance(){
        if (instance == null) {
            instance = new ConfigManager();
            instance.setTheme(ThemeSetting.THEME_STAR_WARS);
        }
        return instance;
    }

    public static void setInstance(ConfigManager configManager) {
        instance=configManager;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
