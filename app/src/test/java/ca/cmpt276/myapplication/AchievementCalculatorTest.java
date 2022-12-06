package ca.cmpt276.myapplication;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import ca.cmpt276.myapplication.model.AchievementCalculator;

public class AchievementCalculatorTest {

    // getBoundaries tests
    @Test
    public void testAllZero(){
        int[] correctOutput = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 0, 0, 0, 100.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }

    @Test
    public void testAverageInput(){
        int[] correctOutput = new int[]{100, 118, 136, 154, 172, 190, 208, 225};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 1, 100, 225, 100.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }

    @Test
    public void testAverageInputReversed(){
        int[] correctOutput = new int[]{225, 208, 190, 172, 154, 136, 118, 100};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 1, 225, 100, 100.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }

    @Test
    public void testSmallRange(){
        int[] correctOutput = new int[]{20, 22, 23, 25, 26, 28, 29, 30};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 1, 20, 30, 100.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }

    // getScorePlacement - Poor score < Great score
    @Test
    public void testAchievedLevelZero(){
        assertEquals(0, AchievementCalculator.getPosition(
                8, 1, 55, 625, 54, 100.0f));
    }

    @Test
    public void testAchievedLevelOne(){
        assertEquals(1, AchievementCalculator.getPosition(
                8, 1, 55, 625, 55, 100.0f));
    }

    @Test
    public void testAchievedLevelEight(){
        assertEquals(8, AchievementCalculator.getPosition(
                8, 1, 55, 625, 625, 100.0f));
    }

    @Test
    public void testAchievedLevelEightExceeded(){
        assertEquals(8, AchievementCalculator.getPosition(
                8, 1, 55, 625, 626, 100.0f));
    }

    // getScorePlacement - Poor score > Great score
    @Test
    public void testAchievedLevelZeroReversed(){
        assertEquals(0, AchievementCalculator.getPosition(
                8, 1, 800, 5, 801, 100.0f));
    }

    @Test
    public void testAchievedLevelOneReversed(){
        assertEquals(1, AchievementCalculator.getPosition(
                8, 1, 800, 5, 800, 100.0f));
    }

    @Test
    public void testAchievedLevelEightReversed(){
        assertEquals(8, AchievementCalculator.getPosition(
                8, 1, 800, 5, 5, 100.0f));
    }

    @Test
    public void testAchievedLevelEightExceededReversed(){
        assertEquals(8, AchievementCalculator.getPosition(
                8, 1, 800, 5, 4, 100.0f));
    }

    // applyDifficulty
    @Test
    public void testApplyEasy(){
        int[] correctOutput = new int[]{168, 156, 142, 129, 115, 102, 88, 75};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 1, 225, 100, 75.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }

    @Test
    public void testApplyHard(){
        int[] correctOutput = new int[]{281, 260, 237, 215, 192, 170, 147, 125};
        List<Integer> resultOutput = AchievementCalculator.getBoundaries(8, 1, 225, 100, 125.0f);

        assertEquals(correctOutput.length, resultOutput.size());

        for (int i = 0; i < correctOutput.length; i++) {
            int x = resultOutput.get(i);
            assertEquals(correctOutput[i], x);
        }
    }
}
