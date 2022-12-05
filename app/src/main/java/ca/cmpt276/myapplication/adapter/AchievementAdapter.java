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
import ca.cmpt276.myapplication.ThemeSetting;
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.ConfigManager;

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

        TextView achievementName = convertView.findViewById(R.id.tvAchievementName1);
        TextView minScore = convertView.findViewById(R.id.tvMinScore);

        final AchievementLevel achievementLevel = getItem(position);
        minScore.setText("Score Required: "+achievementLevel.getBoundary());
        achievementName.setText(achievementLevel.getName());

        ImageView winnerImage=convertView.findViewById(R.id.imageViewLevel);
        String theme= ConfigManager.getInstance().getTheme();
        switch(position) {
            case 0:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars1);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout1);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge1);
                break;
            case 1:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars2);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout2);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge2);
                break;
            case 2:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars3);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout3);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge3);
                break;
            case 3:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars4);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout4);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge4);
                break;
            case 4:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars5);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout5);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge5);
                break;
            case 5:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars6);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout6);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge6);
                break;
            case 6:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars7);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout7);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge7);
                break;
            case 7:
                if(theme.equals(ThemeSetting.THEME_STAR_WARS))
                    winnerImage.setImageResource(R.drawable.starwars8);
                else if(theme.equals(ThemeSetting.THEME_FITNESS))
                    winnerImage.setImageResource(R.drawable.workout8);
                else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
                    winnerImage.setImageResource(R.drawable.sponge8);
                break;
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
