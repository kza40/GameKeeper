package ca.cmpt276.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.Game;

/**
 * AchievementAdapter class enables a complex ListView for previewing the achievement levels of
 * a config.
 */

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

        final AchievementLevel achievementLevel = getItem(position);
        minScore.setText("Score Required: "+achievementLevel.getBoundary());
        achievementName.setText(achievementLevel.getName());

        ImageView winnerImage=convertView.findViewById(R.id.imageViewLevel);
        switch(position) {
            case 0:
                winnerImage.setImageResource(R.drawable.level1);
                break;
            case 1:
                winnerImage.setImageResource(R.drawable.level2);
                break;
            case 2:
                winnerImage.setImageResource(R.drawable.level3);
                break;
            case 3:
                winnerImage.setImageResource(R.drawable.level4);
                break;
            case 4:
                winnerImage.setImageResource(R.drawable.level5);
                break;
            case 5:
                winnerImage.setImageResource(R.drawable.level6);
                break;
            case 6:
                winnerImage.setImageResource(R.drawable.level7);
                break;
            case 7:
                winnerImage.setImageResource(R.drawable.level8);
                break;
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
