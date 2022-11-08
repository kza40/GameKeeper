package ca.cmpt276.myapplication.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains methods that calculate: the boundaries for a set of achievements,
 * and the achievement earned given a score.
 */

public class AchievementCalculator {
    public static final int NUM_ACHIEVEMENTS = 8;

    public static List<Integer> getBoundaries(int numPlayers, int poorScore, int greatScore) {
        List<Integer> boundaries = new ArrayList<>();

        int lowerBound = Math.min(numPlayers * poorScore, numPlayers * greatScore);
        boundaries.add(lowerBound);

        int upperBound = Math.max(numPlayers * poorScore, numPlayers * greatScore);

        int numIntervals = NUM_ACHIEVEMENTS - 1;
        int range = upperBound - lowerBound;
        int jumpAmt = range / numIntervals;

        int extraAmt = range % numIntervals;
        int indexToBeAdjusted = 0;
        int timesAdjusted = 0;

        int currScore = lowerBound;
        for (int i = 0; i < NUM_ACHIEVEMENTS - 2; i++) {
            currScore += jumpAmt;
            if (i == indexToBeAdjusted && timesAdjusted < extraAmt) {
                currScore++;
                indexToBeAdjusted += ((NUM_ACHIEVEMENTS - 2) /  extraAmt);
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

    public static String getAchievementEarned(String[] names, int numPlayers, int poorScore, int greatScore, int groupScore) {
        boolean isReversed = false;
        List<Integer> boundaries = getBoundaries(numPlayers, poorScore, greatScore);
        int index = 0;

        if (poorScore > greatScore) {
            isReversed = true;
        }

        while(index < NUM_ACHIEVEMENTS) {
            if (isReversed && groupScore > boundaries.get(index)) {
                break;
            }
            else if (!isReversed && groupScore < boundaries.get(index)) {
                break;
            }
            index++;
        }
        return names[index];
    }
}
