package ca.cmpt276.myapplication.ui_features;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ca.cmpt276.myapplication.R;

/**
 * This class sets up the option for user to toggle difficulty level for game plays.
 * There are three levels: easy (default), normal, hard.
 */

public class DifficultyToggle {
    private final ImageButton ibStar1;
    private final ImageButton ibStar2;
    private final ImageButton ibStar3;
    private boolean isGoldSecond;
    private boolean isGoldThird;
    private final TextView tvDifficulty;

    private static final float EASY_PERCENT = 75;
    private static final float NORMAL_PERCENT = 100;
    private static final float HARD_PERCENT = 125;

    public DifficultyToggle(View view) {
        tvDifficulty = view.findViewById(R.id.tvDifficulty);
        ibStar1 = view.findViewById(R.id.star1);
        ibStar2 = view.findViewById(R.id.star2);
        ibStar3 = view.findViewById(R.id.star3);
        isGoldSecond = true;
        isGoldThird = false;
    }

    public void setup() {
        ibStar1.setOnClickListener(view -> {
            isGoldSecond = false;
            isGoldThird = false;
            ibStar2.setImageResource(R.drawable.star_grey);
            ibStar3.setImageResource(R.drawable.star_grey);
            updateDifficultyText();
        });

        ibStar2.setOnClickListener(view -> {
            isGoldSecond = true;
            isGoldThird = false;
            ibStar2.setImageResource(R.drawable.star_gold);
            ibStar3.setImageResource(R.drawable.star_grey);
            updateDifficultyText();
        });

        ibStar3.setOnClickListener(view -> {
            isGoldSecond = true;
            isGoldThird = true;
            ibStar2.setImageResource(R.drawable.star_gold);
            ibStar3.setImageResource(R.drawable.star_gold);
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

    public void setDifficulty(float scaleFactor) {
        if(scaleFactor==EASY_PERCENT)
        {
            tvDifficulty.setText(R.string.difficulty_easy);
            isGoldSecond = false;
            isGoldThird = false;
            ibStar2.setImageResource(R.drawable.star_grey);
            ibStar3.setImageResource(R.drawable.star_grey);
        }
        else if(scaleFactor==NORMAL_PERCENT)
        {
            tvDifficulty.setText(R.string.difficulty_normal);
            isGoldSecond = true;
            isGoldThird = false;
            ibStar2.setImageResource(R.drawable.star_gold);
            ibStar3.setImageResource(R.drawable.star_grey);
        }
        else if(scaleFactor==HARD_PERCENT)
        {
            tvDifficulty.setText(R.string.difficulty_hard);
            isGoldSecond = true;
            isGoldThird = true;
            ibStar2.setImageResource(R.drawable.star_gold);
            ibStar3.setImageResource(R.drawable.star_gold);
        }
        else
        {
            tvDifficulty.setText(R.string.difficulty_normal);
            isGoldSecond = true;
            isGoldThird = false;
            ibStar2.setImageResource(R.drawable.star_gold);
            ibStar3.setImageResource(R.drawable.star_grey);
        }
        updateDifficultyText();
    }
}
