package ca.cmpt276.myapplication;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This class sets up the option for user to toggle difficulty level for game plays.
 * There are three levels: easy (default), normal, hard.
 */

public class DifficultyToggle {
    private ImageButton ibStar2;
    private ImageButton ibStar3;
    private boolean isGoldSecond;
    private boolean isGoldThird;
    private TextView tvDifficulty;
    private final View inputView;

    private static final float EASY_PERCENT = 75;
    private static final float NORMAL_PERCENT = 100;
    private static final float HARD_PERCENT = 125;

    public DifficultyToggle(View view) {
        inputView = view;
        tvDifficulty = inputView.findViewById(R.id.tvDifficulty);
        ibStar2 = inputView.findViewById(R.id.star2);
        ibStar3 = inputView.findViewById(R.id.star3);
        isGoldSecond = true;
        isGoldThird = false;
    }

    public void setup() {
        tvDifficulty = inputView.findViewById(R.id.tvDifficulty);
        ibStar2 = inputView.findViewById(R.id.star2);
        ibStar3 = inputView.findViewById(R.id.star3);

        isGoldSecond = true;
        isGoldThird = false;

        ibStar2.setOnClickListener(view -> {
            if (isGoldSecond && !isGoldThird) {
                ibStar2.setImageResource(R.drawable.star_grey);
                isGoldSecond = false;
            } else if (!isGoldSecond && !isGoldThird){
                ibStar2.setImageResource(R.drawable.star_gold);
                isGoldSecond = true;
            }
            updateDifficultyText();
        });

        ibStar3.setOnClickListener(view -> {
            if (!isGoldThird && isGoldSecond) {
                ibStar3.setImageResource(R.drawable.star_gold);
                isGoldThird = true;
            } else if (isGoldThird && isGoldSecond){
                ibStar3.setImageResource(R.drawable.star_grey);
                isGoldThird = false;
            }
            updateDifficultyText();
        });
    }

    private void updateDifficultyText() {
        if (!isGoldSecond) {
            tvDifficulty.setText(R.string.difficulty_easy);
        }
        else if (!isGoldThird) {
            tvDifficulty.setText(R.string.difficulty_normal);
        }
        else {
            tvDifficulty.setText(R.string.difficulty_hard);
        }
    }

    public float getScaleFactor() {
        if (!isGoldSecond) {
            return EASY_PERCENT;
        }
        else if (!isGoldThird) {
            return NORMAL_PERCENT;
        }
        else {
            return HARD_PERCENT;
        }
    }
}
