package ca.cmpt276.myapplication.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains methods that calculate: the boundaries for a set of achievements,
 * the achievement earned given a score.
 */

public class AchievementCalculator {
    public static final int INDEX_SUB_LEVEL_ONE = -1;

    // constraint on numAchievements, must be >= 2
    public static List<Integer> getBoundaries(int numAchievements, int numPlayers, int poorScore, int greatScore) {
        List<Integer> boundaries = new ArrayList<>();

        int lowerBound = Math.min(numPlayers * poorScore, numPlayers * greatScore);
        boundaries.add(lowerBound);

        int upperBound = Math.max(numPlayers * poorScore, numPlayers * greatScore);

        int numIntervals = numAchievements - 1;
        int range = upperBound - lowerBound;
        int jumpAmt = range / numIntervals;

        int extraAmt = range % numIntervals;
        int indexToBeAdjusted = 0;
        int timesAdjusted = 0;

        int currScore = lowerBound;
        for (int i = 0; i < numAchievements - 2; i++) {
            currScore += jumpAmt;
            if (i == indexToBeAdjusted && timesAdjusted < extraAmt) {
                currScore++;
                indexToBeAdjusted += ((numAchievements - 2) /  extraAmt);
                timesAdjusted++;
            }
            boundaries.add(currScore);
        }
        boundaries.add(upperBound);

        if (poorScore > greatScore) {
            Collections.reverse(boundaries);
        }
        return boundaries;
    }

    public static int getScorePlacement(int numAchievements, int numPlayers, int poorScore, int greatScore, int groupScore, float scaleFactor) {
        boolean isReversed = false;
        List<Integer> boundaries = getBoundaries(numAchievements, numPlayers, poorScore, greatScore);
        applyDifficulty(boundaries, scaleFactor);

        int index = 0;
        int placement = -1;

        if (poorScore > greatScore) {
            isReversed = true;
        }

        while(index < numAchievements) {
            if (isReversed && groupScore > boundaries.get(index)) {
                break;
            }
            else if (!isReversed && groupScore < boundaries.get(index)) {
                break;
            }
            placement++;
            index++;
        }

        return placement;
    }

    public static void applyDifficulty(List<Integer> boundaries, float scaleFactor) {
        for (int i = 0; i < boundaries.size(); i++) {
            int x = boundaries.get(i);
            x = (int)(x * (scaleFactor/100.0f));
            boundaries.set(i, x);
        }
    }
}
