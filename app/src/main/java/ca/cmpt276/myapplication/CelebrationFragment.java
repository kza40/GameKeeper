package ca.cmpt276.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.GameConfig;

public class CelebrationFragment extends AppCompatDialogFragment {

    private ConfigManager configManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View tv = LayoutInflater.from(getActivity()).inflate(R.layout.celebration_fragment, null);



        configManager = ConfigManager.getInstance();

        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.sample);
        mp.start();
        setupAnimations(tv);

        showAchievementLvl();




        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations")
                .setView(tv)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

    private void showAchievementLvl() {
//        String temp = AchievementCalculator
//                .getAchievementEarned(ConfigManager., Integer.parseInt(numPlayersInput),
//                        gameConfig.getPoorScore(), gameConfig.getGoodScore(),
//                        Integer.parseInt(scoreInput));
//        TextView showAchievement = getActivity().findViewById(R.id.tvAchievement);
//        String message = temp + getString(R.string.exclamation);
//        showAchievement.setText(message);
    }


    private void setupAnimations(View tv) {
        ImageView rSaber;
        ImageView gSaber;

        rSaber = tv.findViewById(R.id.rSaber);
        gSaber = tv.findViewById(R.id.gSaber);

        Animation rotateSlideR = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_rotate_right);

        rSaber.startAnimation(rotateSlideR);
        gSaber.startAnimation(rotateSlideL);
    }
}
