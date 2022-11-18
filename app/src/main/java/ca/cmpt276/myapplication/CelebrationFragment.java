package ca.cmpt276.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CelebrationFragment extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View tv = LayoutInflater.from(getActivity()).inflate(R.layout.celebration_fragment, null);

        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.sample);
        mp.start();
        setupAnimations();



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

    private void setupAnimations() {
        ImageView rSaber;
        ImageView gSaber;

        rSaber = getActivity().findViewById(R.id.rSaber);
        gSaber = getActivity().findViewById(R.id.gSaber);

        Animation rotateSlideR = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_rotate_left);
        Animation rotateSlideL = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_rotate_right);

        rSaber.startAnimation(rotateSlideR);
        gSaber.startAnimation(rotateSlideL);
    }
}
