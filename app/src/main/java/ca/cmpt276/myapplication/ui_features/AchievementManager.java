package ca.cmpt276.myapplication.ui_features;

import android.view.View;

import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.ThemeSetting;
import ca.cmpt276.myapplication.model.AchievementCalculator;

/**
 * This class handles the UI aspect for achievements and achievement themes.
 */

public class AchievementManager {
    private String[] titles;
    private String titleSubLevelOne;
    private final View view;
    private String theme;
    public static final int NUMBER_OF_ACHIEVEMENT_POS = 8;

    public AchievementManager(View view, String theme) {
        this.view = view;
        this.theme = theme;
        setTitles();
    }

    public int getAchievementPos(int totalScore, int playerCount, int poorScore, int goodScore, float scaleFactor) {
        return AchievementCalculator.getScorePlacement(titles.length, playerCount, poorScore, goodScore, totalScore, scaleFactor);
    }

    public int getPointsToNextLevel(int totalScore, int numPlayers, int poorScore, int goodScore){
        return AchievementCalculator.getPointsToNextLevel(totalScore, titles.length, numPlayers, poorScore, goodScore);
    }

    public void setTitles() {
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            titles = view.getResources().getStringArray(R.array.theme_fitness_names);
            titleSubLevelOne = view.getResources().getString(R.string.fitnessLvl0);

        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            titles = view.getResources().getStringArray(R.array.theme_spongebob_names);
            titleSubLevelOne = view.getResources().getString(R.string.spongeBobLvl0);

        } else {
            titles = view.getResources().getStringArray(R.array.theme_starwars_names);
            titleSubLevelOne = view.getResources().getString(R.string.starWarsLvl0);
        }
    }

    public void updateTheme(String theme) {
        this.theme = theme;
        setTitles();
    }

    public String getAchievementAtIndex(int index) {
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            return titleSubLevelOne;
        } else {
            return titles[index];
        }
    }

    public String getNextHighestAchievement(int index) {
        if (index == titles.length - 1) {
            return titles[index];
        }
        return titles[index+1];
    }

}
