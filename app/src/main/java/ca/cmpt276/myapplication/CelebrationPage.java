package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.ui_features.AchievementManager;

public class CelebrationPage extends AppCompatActivity {
    private static final String ACHIEVEMENT_POS = "CelebrationPage: Achievement pos";
    private static final String BOUNDARY_DIFFERENCE = "CelebrationPage: nextBoundary difference";

    private ConfigManager configManager;
    private AchievementManager achievementManager;
    private String theme;
    private int achievementPos;
    private int nextScoreDifference;

    private ImageView ivReload;
    private TextView tvAchievementEarned;
    private TextView tvNextAchievement;
    private ImageView leftItem;
    private ImageView rightItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration_page_general);

        nextScoreDifference = getIntent().getIntExtra(BOUNDARY_DIFFERENCE, -1);
        achievementPos = getIntent().getIntExtra(ACHIEVEMENT_POS, -1);

        configManager = ConfigManager.getInstance();
        tvAchievementEarned = findViewById(R.id.tvAchievementName);
        tvNextAchievement = findViewById(R.id.tvNextAchievementMessage);
        ivReload = findViewById(R.id.ivReload);

        setContentTheme();
        View view = findViewById(android.R.id.content).getRootView();
        achievementManager = new AchievementManager(view, theme);

        startEffects();
        setupReload();
        setupMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentTheme();
        achievementManager.updateTheme(theme);
        setupMessages();
    }

    private void setContentTheme() {
        theme = configManager.getTheme();

        ImageView bg = findViewById(R.id.ivBackground);
        leftItem = findViewById(R.id.leftAnimImage);
        rightItem = findViewById(R.id.rightAnimImage);

        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            bg.setImageResource(R.drawable.fitness_bg);
            leftItem.setImageResource(R.drawable.dumbbell);
            rightItem.setImageResource(R.drawable.dumbbell);

        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            bg.setImageResource(R.drawable.sponge_background);
            leftItem.setImageResource(R.drawable.right_garry);
            rightItem.setImageResource(R.drawable.left_garry);

        } else {
            bg.setImageResource(R.drawable.starwars_background);
            leftItem.setImageResource(R.drawable.saber_flipped);
            rightItem.setImageResource(R.drawable.saber);
        }
    }

    private void startEffects() {
        MediaPlayer mp = MediaPlayer.create(CelebrationPage.this, R.raw.win_sound);
        mp.start();

        Animation rotateSlideR = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_right);

        rightItem.startAnimation(rotateSlideR);
        leftItem.startAnimation(rotateSlideL);
    }

    private void setupReload() {
        ivReload.setOnClickListener(view -> startEffects());
    }

    private void setupMessages() {
        String currentAchievement = getString(R.string.you_got) + achievementManager.getAchievementAtIndex(achievementPos) + "!";
        tvAchievementEarned.setText(currentAchievement);

        if (nextScoreDifference == 0) {
            tvNextAchievement.setText(R.string.highest_achievement);
        }
        else {
            String nextAchievementDifference = getString(R.string.you_were) + nextScoreDifference + getString(R.string.points_away_from)
                              + achievementManager.getAchievementAtIndex(achievementPos + 1);
            tvNextAchievement.setText(nextAchievementDifference);
        }
    }

    public void onSettingSelected(View view){
        Intent intent = ThemeSetting.makeIntent(this);
        startActivity(intent);
    }

    public static Intent makeIntent(Context context, int achievementPos, int boundaryDifference) {
        Intent intent = new Intent(context, CelebrationPage.class);
        intent.putExtra(ACHIEVEMENT_POS, achievementPos);
        intent.putExtra(BOUNDARY_DIFFERENCE, boundaryDifference);
        return intent;
    }

}
