package ca.cmpt276.myapplication.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Achievements {
    public static final int NUM_ACHIEVEMENTS = 8;

    public static List<Integer> getBoundaries(int numPlayers, int poorScore, int greatScore) {
        List<Integer> boundaries = new ArrayList<>();

        int lowerBound = Math.min(numPlayers * poorScore, numPlayers * greatScore);
        boundaries.add(lowerBound);

        int upperBound = Math.max(numPlayers * poorScore, numPlayers * greatScore);

        int numIntervals = NUM_ACHIEVEMENTS - 1;
        int range = upperBound - lowerBound;
        // Approximately how much each level should be spaced out (rounded down)
        int jumpAmt = range / numIntervals;

        // In case jumpAmt calculation is not a whole number, the space between Level 7 and Level 8
        // will be larger than the jumpAmt. The rest of the levels will be adjusted

        int extraAmt = range % numIntervals;     // Between 0 to 6
        int indexToBeAdjusted = 0;      // Start adjusting the levels from Level 2
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

        while(index < Achievements.NUM_ACHIEVEMENTS) {
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
