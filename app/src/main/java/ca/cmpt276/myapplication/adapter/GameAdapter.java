package ca.cmpt276.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.model.Game;

/**
 * GameAdapter class enables a complex ListView for viewing the games within a config.
 */

public class GameAdapter extends ArrayAdapter<Game> {
    private Context context;
    private int resource;

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

        TextView achievementView = convertView.findViewById(R.id.tvAchievementView);
        TextView dateView = convertView.findViewById(R.id.tvDateView);
        TextView numPlayersView = convertView.findViewById(R.id.tvNumPlayersView);
        TextView groupScore = convertView.findViewById(R.id.tvScoreView);

        achievementView.setText(getItem(position).getAchievementEarned());
        dateView.setText(getItem(position).getDatePlayed());
        String numPlayers = getItem(position).getNumOfPlayers() + " players";
        numPlayersView.setText(numPlayers);
        groupScore.setText("Score: " + getItem(position).getGroupScore());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
