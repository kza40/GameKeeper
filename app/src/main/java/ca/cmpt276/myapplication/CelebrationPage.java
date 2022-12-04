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

    private String nextAchievement;
    private int nextScoreDifference;
    private TextView tvNextAchievement;
//    private TextView tvNextDifference;

    // UI views
    private ImageView ivReload;
    private TextView tvName;
    private ImageView leftItem;
    private ImageView rightItem;

    //todo: display points till the next achievement
    //todo: change color of texts based on theme to see better


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration_page_general);

        configManager = ConfigManager.getInstance();
        tvName = findViewById(R.id.tvAchievementName);
        tvNextAchievement = findViewById(R.id.tvNextAchievementMessage);
        ivReload = findViewById(R.id.ivReload);

        setContentTheme();
        View view = findViewById(android.R.id.content).getRootView();
        achievementManager = new AchievementManager(view, theme);

        showAchievementEarned();
        startEffects();
        setupReload();

//        findNextAchievement();
//        setNextBoundary();
//        tvNextAchievement.setText(nextAchievement);

//        nextScoreDifference = getIntent().getIntExtra(BOUNDARY_DIFFERENCE, -1);
//        tvNextDifference.setText(nextScoreDifference);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentTheme();
        achievementManager.updateTheme(theme);
        showAchievementEarned();
    }

    private void showAchievementEarned() {
        int pos = getIntent().getIntExtra(ACHIEVEMENT_POS, -1);
        String message = achievementManager.getAchievementAtIndex(pos) + "!";
        tvName.setText(message);
    }

//    private void setNextBoundary() {
//        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
//            tvNextDifference = findViewById(R.id.tvScoreDifference2);
//        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
//            tvNextDifference = findViewById(R.id.tvScoreDifference1);
//        } else {
//            tvNextDifference = findViewById(R.id.tvScoreDifference);
//        }
//    }

    private void setContentTheme() {
        theme = configManager.getTheme();

        ImageView bg = findViewById(R.id.ivBackground);
        leftItem = findViewById(R.id.leftAnimImage);
        rightItem = findViewById(R.id.rightAnimImage);

        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            bg.setImageResource(R.drawable.fitness_background);
            leftItem.setImageResource(R.drawable.left_dumbbell);
            rightItem.setImageResource(R.drawable.right_dumbbell);

        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            bg.setImageResource(R.drawable.sponge_background);
            leftItem.setImageResource(R.drawable.right_garry);
            rightItem.setImageResource(R.drawable.left_garry_new);

        } else {
            bg.setImageResource(R.drawable.starwars_background);
            leftItem.setImageResource(R.drawable.green_saber);
            rightItem.setImageResource(R.drawable.green_saber);
        }
    }

//    private void findNextAchievement() {
//        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
//            themeTitles = getResources().getStringArray(R.array.theme_fitness_names);
//            tvNextAchievement = findViewById(R.id.tvNextAchievement2);
//        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
//            themeTitles = getResources().getStringArray(R.array.theme_spongebob_names);
//            tvNextAchievement = findViewById(R.id.tvNextAchievement1);
//        } else {
//            themeTitles = getResources().getStringArray(R.array.theme_starwars_names);
//            tvNextAchievement = findViewById(R.id.tvNextAchievement);
//        }
//        for(int i=0; i< themeTitles.length; i++){
//            if(themeTitles[i]==achievement){
//                nextAchievement= themeTitles[i++];
//            }
//        }
//    }


    public void onSettingSelected(View view){
        Intent intent = ThemeSetting.makeIntent(this);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startEffects() {
        MediaPlayer mp = MediaPlayer.create(CelebrationPage.this, R.raw.win_sound);
        mp.start();

//        ImageView falconer;
//        if(theme.equals(ThemeSetting.STAR_WARS)){
//            falconer = findViewById(R.id.falconer);
//            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.up);
//            falconer.startAnimation(slideUp);
//        }

        Animation rotateSlideR = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_right);

        rightItem.startAnimation(rotateSlideR);
        leftItem.startAnimation(rotateSlideL);
    }

    private void setupReload() {
        ivReload.setOnClickListener(view -> startEffects());
    }

    public static Intent makeIntent(Context context, int achievementPos, int boundaryDifference) {
        Intent intent = new Intent(context, CelebrationPage.class);
        intent.putExtra(ACHIEVEMENT_POS, achievementPos);
        intent.putExtra(BOUNDARY_DIFFERENCE, boundaryDifference);
        return intent;
    }

}
