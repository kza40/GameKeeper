package ca.cmpt276.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CelebrationFragment extends AppCompatDialogFragment {

    private String name;

    public CelebrationFragment(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.celebration_fragment, null);

        MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.sample);
        mp.setVolume(1.0f,1.0f);
        mp.start();
        setupAnimations(view);
        TextView txtView = view.findViewById(R.id.txtViewLevel);
        txtView.setText(name);

        DialogInterface.OnClickListener listener = (dialogInterface, i) -> getActivity().finish();

        return new AlertDialog.Builder(getActivity())
                .setTitle("Congratulations!")
                .setView(view)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
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
