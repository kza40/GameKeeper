package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.myapplication.model.ConfigManager;

public class CelebrationPage extends AppCompatActivity {

    private static final String ACHIEVEMENT_NAME = "CelebrationPage: Achievement name";
    private String nextAchievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigManager configManager = ConfigManager.getInstance();
        String theme = configManager.getTheme();

        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            setContentView(R.layout.activity_celebration_page_fitness);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            setContentView(R.layout.activity_celebration_page_spongebob);
        } else {
            setContentView(R.layout.activity_celebration_page_starwars);
        }


//        TextView tvName = findViewById(R.id.tvAchievementName);
//        String name = getIntent().getStringExtra(ACHIEVEMENT_NAME);
//        tvName.setText(name);
//
//        findNextAchievement();
//        startEffects();
//        setupReload();
    }

    private void startEffects() {
        MediaPlayer mp = MediaPlayer.create(CelebrationPage.this, R.raw.win_sound);
        mp.start();

        ImageView leftAnim;
        ImageView rightAnim;

        leftAnim = findViewById(R.id.rSaber);
        rightAnim = findViewById(R.id.gSaber);

        Animation rotateSlideR = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_right);

        leftAnim.startAnimation(rotateSlideR);
        rightAnim.startAnimation(rotateSlideL);
    }

    private void setupReload() {
        ImageView ivReload = findViewById(R.id.ivReload);
        ivReload.setOnClickListener(view -> startEffects());
    }

    public static Intent makeIntent(Context context, String achievementName) {
        Intent intent = new Intent(context, CelebrationPage.class);
        intent.putExtra(ACHIEVEMENT_NAME, achievementName);
        return intent;
    }

}