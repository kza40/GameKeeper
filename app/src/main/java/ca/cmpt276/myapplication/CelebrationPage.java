package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.myapplication.model.ConfigManager;

public class CelebrationPage extends AppCompatActivity {

    private static final String ACHIEVEMENT_NAME = "CelebrationPage: Achievement name";
    private static final String BOUNDARY_DIFFERENCE = "CelebrationPage: nextBoundary difference";
    private String achievement;
    private String nextAchievement;
    private int nextScoreDifference;
    private String theme;
    private String[] themeTitles;
    private ConfigManager configManager;
    ImageView ivReload;
    TextView tvName;
    TextView tvNextAchievement;
    TextView tvNextDifference;

    //todo: findNextAchievement, Ui and the score til the next achievement
    //todo: the onResume bug


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configManager = ConfigManager.getInstance();
        theme = configManager.getTheme();


        setContentTheme();
        findNextAchievement();
        setNextBoundary();

        tvNextAchievement.setText(nextAchievement);

//        nextScoreDifference = getIntent().getIntExtra(BOUNDARY_DIFFERENCE, -1);
//        tvNextDifference.setText(nextScoreDifference);

        achievement = getIntent().getStringExtra(ACHIEVEMENT_NAME);
        tvName.setText(achievement);
//
//        setNextBoundary();

        startEffects();
        setupReload();
    }

    private void setNextBoundary() {
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            tvNextDifference = findViewById(R.id.tvScoreDifference2);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            tvNextDifference = findViewById(R.id.tvScoreDifference1);
        } else {
            tvNextDifference = findViewById(R.id.tvScoreDifference);
        }
    }

    private void setContentTheme() {
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            setContentView(R.layout.activity_celebration_page_fitness);
            tvName = findViewById(R.id.tvAchievementName2);
            ivReload = findViewById(R.id.ivReload2);

        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            setContentView(R.layout.activity_celebration_page_spongebob);
            tvName = findViewById(R.id.tvAchievementName1);
            ivReload = findViewById(R.id.ivReload1);

        } else {
            setContentView(R.layout.activity_celebration_page_starwars);
            tvName = findViewById(R.id.tvAchievementName);
            ivReload = findViewById(R.id.ivReload);
        }
    }

    private void findNextAchievement() {
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            themeTitles = getResources().getStringArray(R.array.theme_fitness_names);
            tvNextAchievement = findViewById(R.id.tvNextAchievement2);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            themeTitles = getResources().getStringArray(R.array.theme_spongebob_names);
            tvNextAchievement = findViewById(R.id.tvNextAchievement1);
        } else {
            themeTitles = getResources().getStringArray(R.array.theme_starwars_names);
            tvNextAchievement = findViewById(R.id.tvNextAchievement);
        }
        for(int i=0; i< themeTitles.length; i++){
            if(themeTitles[i]==achievement){
                nextAchievement= themeTitles[i++];
            }
        }
    }

//gotta check this


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentTheme();
        startEffects();
        setupReload();
    }

    public void onSettingSelected(View view){
        Intent intent = ThemeSetting.makeIntent(this);
        startActivity(intent);
    }

    private void startEffects() {
        MediaPlayer mp = MediaPlayer.create(CelebrationPage.this, R.raw.win_sound);
        mp.start();

        ImageView leftAnim;
        ImageView rightAnim;
        ImageView falconer;
        if(theme.equals(ThemeSetting.THEME_FITNESS)){
            leftAnim = findViewById(R.id.right_dumbbell);
            rightAnim = findViewById(R.id.left_dumbbell);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)){
            leftAnim = findViewById(R.id.right_party);
            rightAnim = findViewById(R.id.left_party);
        } else {
            leftAnim = findViewById(R.id.right_spaceship);
            rightAnim = findViewById(R.id.left_spaceship);
            falconer = findViewById(R.id.falconer);
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.up);
            falconer.startAnimation(slideUp);
        }


        Animation rotateSlideR = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_right);

        leftAnim.startAnimation(rotateSlideR);
        rightAnim.startAnimation(rotateSlideL);
    }

    private void setupReload() {
        ivReload.setOnClickListener(view -> startEffects());
    }

    public static Intent makeIntent(Context context, String achievementName, int nextBoundary) {
        Intent intent = new Intent(context, CelebrationPage.class);
        intent.putExtra(ACHIEVEMENT_NAME, achievementName);
        intent.putExtra(BOUNDARY_DIFFERENCE, nextBoundary);
        return intent;
    }

}