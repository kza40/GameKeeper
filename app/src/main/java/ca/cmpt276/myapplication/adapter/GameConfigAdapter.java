package ca.cmpt276.myapplication.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.List;

import ca.cmpt276.myapplication.AchievementStatistics;
import ca.cmpt276.myapplication.AddConfig;
import ca.cmpt276.myapplication.PreviewAchievements;
import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.ViewConfigs;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

/**
 * GameConfigAdapter class enables a complex ListView for viewing, editing, deleting, and
 * previewing achievements of a game configuration.
 */

public class GameConfigAdapter extends ArrayAdapter<GameConfig> {
    private static final String TAG="GamesListAdapter";
    private Context context;
    private int resource;
    public final String APP_TAG = "MyCustomApp";

    public GameConfigAdapter(@NonNull Activity context, int resource, @NonNull List<GameConfig> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GameConfig gameConfig =getItem(position);
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        TextView title=convertView.findViewById(R.id.tvTitle);
        TextView poorScore=convertView.findViewById(R.id.tvPoorScore);
        TextView goodScore=convertView.findViewById(R.id.tvGoodScore);
        ImageButton btnAchievementStats=convertView.findViewById(R.id.btnAchievementStats);
        ImageButton btnAchievement=convertView.findViewById(R.id.btnAchievement);
        ImageButton btnEdit=convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete=convertView.findViewById(R.id.btnDelete);

        ImageView imageViewConfigPhoto=convertView.findViewById(R.id.ivConfigPhoto);
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File photoFile = new File(mediaStorageDir.getPath() + File.separator + getItem(position).getPhotoFileName());
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        if(takenImage!=null)
        {
            imageViewConfigPhoto.setImageBitmap(takenImage);
        }
        else
        {
            imageViewConfigPhoto.setImageResource(R.drawable.default_config);
        }

        title.setText(gameConfig.getConfigTitle());
        poorScore.setText(context.getString(R.string.poor_score_colon)+Integer.toString(gameConfig.getPoorScore()));
        goodScore.setText(context.getString(R.string.good_score_colon)+Integer.toString(gameConfig.getGoodScore()));
        setupClickListenersOnButton(gameConfig,parent,position,btnEdit,btnDelete,btnAchievement, btnAchievementStats);
        return convertView;
    }

    private void setupClickListenersOnButton(GameConfig gameConfig, ViewGroup parent, int position, ImageButton btnEdit, ImageButton btnDelete, ImageButton btnAchievement, ImageButton btnAchievementStats) {
        btnAchievementStats.setOnClickListener((View view)->{
            ConfigManager configManager = ConfigManager.getInstance();
            Intent intent = AchievementStatistics.makeIntent(context, gameConfig.getAchievementPosCounter(), configManager.getTheme());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        btnAchievement.setOnClickListener((View view)->{
            Intent intent= PreviewAchievements.makeIntent(context,gameConfig.getPoorScore(),gameConfig.getGoodScore());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        btnEdit.setOnClickListener((View view) ->{
                Intent intent=AddConfig.makeIntent(context,true,position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
        });

        btnDelete.setOnClickListener((View view) -> {
            showConfirmDeleteDialogBox(position);
        });
    }

    private void showConfirmDeleteDialogBox(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_dialog_message)
                .setTitle(R.string.confirm_dialog_title)
                .setPositiveButton(R.string.confirm, (DialogInterface dialogInterface, int i) ->{

                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                ConfigManager configManager=ConfigManager.getInstance();
                                configManager.removeConfigAtIndex(position);
                                new SharedPreferenceManager(context).updateConfigManager(configManager);
                                notifyDataSetChanged();
                                ((ViewConfigs)context).setEmptyState();
                                break;
                        }
                }).
                setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
