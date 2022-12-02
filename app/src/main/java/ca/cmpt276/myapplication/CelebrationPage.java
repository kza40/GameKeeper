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
    private String achievement;
    private String nextAchievement;
    private String theme;
    private String[] themeTitles;
    private ConfigManager configManager;
    ImageView ivReload;
    TextView tvName;

    //todo: findNextAchievement, Ui and the score til the next achievement
    //todo: Put helper margins for UI
    //todo: the onResume bug


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configManager = ConfigManager.getInstance();
        theme = configManager.getTheme();


        setContentTheme();

        achievement = getIntent().getStringExtra(ACHIEVEMENT_NAME);
        tvName.setText(achievement);

//        findNextAchievement();

        startEffects();
        setupReload();
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

//    private void findNextAchievement() {
//        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
//            themeTitles = getResources().getStringArray(R.array.theme_fitness_names);
//        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
//            themeTitles = getResources().getStringArray(R.array.theme_spongebob_names);
//        } else {
//            themeTitles = getResources().getStringArray(R.array.theme_starwars_names);
//        }
//        for(int i=0; i< themeTitles.length; i++){
//            if(themeTitles[i]==achievement){
//                nextAchievement= themeTitles[i++];
//            }
//        }
//        tvNextAchieve.setText(nextAchievement);
//    }

//gotta check this
    @Override
    protected void onResume() {
        super.onResume();
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
//        theme = configManager.getTheme();
        ivReload.setOnClickListener(view -> startEffects());
    }

    public static Intent makeIntent(Context context, String achievementName) {
        Intent intent = new Intent(context, CelebrationPage.class);
        intent.putExtra(ACHIEVEMENT_NAME, achievementName);
        return intent;
    }

}