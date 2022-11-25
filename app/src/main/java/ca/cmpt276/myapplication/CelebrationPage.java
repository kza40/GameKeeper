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

public class CelebrationPage extends AppCompatActivity {

    private String name;
    private String ACHIEVEMENT;

    public CelebrationPage(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration_page);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.sample);
        mp.setVolume(1.0f,1.0f);
        mp.start();

        TextView txtView = findViewById(R.id.txtViewLevel);
        txtView.setText(name);



        setupAnimations();
        setupReload();
    }

    private void setupReload() {
        ImageView reload = findViewById(R.id.reload);

        reload.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, CelebrationPage.class);
        return intent;
    }


    private void setupAnimations() {
        ImageView rSaber;
        ImageView gSaber;

        rSaber = findViewById(R.id.rSaber);
        gSaber = findViewById(R.id.gSaber);

        Animation rotateSlideR = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(this, R.anim.slide_rotate_right);

        rSaber.startAnimation(rotateSlideR);
        gSaber.startAnimation(rotateSlideL);
    }


}