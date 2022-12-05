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
    private final View view;
    private String theme;
    public static final int NUMBER_OF_ACHIEVEMENT_POS = 9;

    public AchievementManager(View view, String theme) {
        this.view = view;
        this.theme = theme;
        setTitles();
    }

    public int getAchievementPos(int totalScore, int playerCount, int poorScore, int goodScore, float scaleFactor) {
        return AchievementCalculator.getPosition(titles.length - 1, playerCount, poorScore, goodScore, totalScore, scaleFactor);
    }

    public int getPointsToNextLevel(int totalScore, int playerCount, int poorScore, int goodScore, float scaleFactor, int achievementPos){
        return AchievementCalculator.getPointsToNextLevel(titles.length - 1, playerCount, poorScore, goodScore, totalScore, scaleFactor, achievementPos);
    }

    public void setTitles() {
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            titles = view.getResources().getStringArray(R.array.theme_fitness_names);

        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            titles = view.getResources().getStringArray(R.array.theme_spongebob_names);

        } else {
            titles = view.getResources().getStringArray(R.array.theme_starwars_names);
        }
    }

    public void updateTheme(String theme) {
        this.theme = theme;
        setTitles();
    }

    public String getAchievementAtIndex(int index) {
        return titles[index];
    }

    public int getNumAchievements() {
        return titles.length;
    }

    public String[] getTitles() {
        return titles;
    }

}
