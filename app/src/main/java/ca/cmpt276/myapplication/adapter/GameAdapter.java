package ca.cmpt276.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.ui_features.AchievementManager;

/**
 * GameAdapter class enables a complex ListView for viewing the games within a config.
 */

public class GameAdapter extends ArrayAdapter<Game> {
    private Context context;
    private int resource;
    private static final float EASY_PERCENT = 75;
    private static final float HARD_PERCENT = 125;

    public GameAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        AchievementManager achievementManager = new AchievementManager(convertView, ConfigManager.getInstance().getTheme());
        TextView achievementView = convertView.findViewById(R.id.tvAchievement);
        String achievementName = achievementManager.getAchievementAtIndex(getItem(position).getAchievementPos());
        achievementView.setText(achievementName);

        TextView dateView = convertView.findViewById(R.id.tvDate);
        dateView.setText(getItem(position).getDatePlayed());

        TextView numPlayersView = convertView.findViewById(R.id.tvNumPlayers);
        String numPlayers = getItem(position).getNumOfPlayers() + context.getString(R.string.players);
        numPlayersView.setText(numPlayers);

        TextView scoreView = convertView.findViewById(R.id.tvScore);
        String score = Integer.toString(getItem(position).getGroupScore());
        scoreView.setText(score);

        ImageView ivStar2 = convertView.findViewById(R.id.star2);
        ImageView ivStar3 = convertView.findViewById(R.id.star3);

        float scaleFactor = getItem(position).getScaleFactor();
        if (scaleFactor == EASY_PERCENT) {
            ivStar2.setImageResource(R.drawable.star_grey);
        } else if (scaleFactor == HARD_PERCENT) {
            ivStar3.setImageResource(R.drawable.star_gold);
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
