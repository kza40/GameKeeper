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
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.Game;

public class AchievementAdapter extends ArrayAdapter<AchievementLevel> {
    private Context context;
    private int resource;

    public AchievementAdapter(@NonNull Context context, int resource, @NonNull List<AchievementLevel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView achievementName = convertView.findViewById(R.id.tvAchievementName);
        TextView minScore = convertView.findViewById(R.id.tvMinScore);

        minScore.setText(getItem(position).getBoundary());
        achievementName.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
