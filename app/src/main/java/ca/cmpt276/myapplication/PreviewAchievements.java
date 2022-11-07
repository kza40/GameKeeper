package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.adapter.AchievementAdapter;
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.Achievements;

public class PreviewAchievements extends AppCompatActivity {
    private static final String EXTRA_POOR_SCORE = "ca.cmpt276.assignment2: poor score";
    private static final String EXTRA_GREAT_SCORE = "ca.cmpt276.assignment2: great score";
    private static final int NUM_ACHIEVEMENTS = 8;

    private String[] titles = new String[] { "Lvl 1", "Lvl 2", "Lvl 3", "Lvl 4", "Lvl 5",
            "Lvl 6", "Lvl 7", "Lvl 8" };
    private List<AchievementLevel> achievementLevels;

    private int poorScore;
    private int greatScore;
    private List<Integer> scoreBoundaries;
    private EditText edtNumPlayers;
    private ListView list;
    private AchievementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_achievements);

        setTitle(R.string.AchievementsTitle);
        setUpMemberVariables();
        setupAchievementLevels();
    }

    private void setUpMemberVariables() {
        Intent intent = getIntent();
        poorScore = intent.getIntExtra(EXTRA_POOR_SCORE, 0);
        greatScore = intent.getIntExtra(EXTRA_GREAT_SCORE, 0);

        scoreBoundaries = new ArrayList<>();

        edtNumPlayers = findViewById(R.id.edtNumPlayers);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);

        list = findViewById(R.id.achievementLevels);
    }

    private void setupAchievementLevels() {
        achievementLevels = new ArrayList<>();
        for(int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            AchievementLevel newLevel = new AchievementLevel(titles[i]);
            achievementLevels.add(newLevel);
        }
        adapter = new AchievementAdapter(this, R.layout.adapter_view3, achievementLevels);
        list.setAdapter(adapter);
    }

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numPlayersInput = edtNumPlayers.getText().toString();
            if(!numPlayersInput.isEmpty()) {
                updateListView(Integer.parseInt(numPlayersInput));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void updateListView(int numPlayers) {
        List<Integer> boundaries = Achievements.getBoundaries(numPlayers, poorScore, greatScore);
        for(int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            String num = Integer.toString(boundaries.get(i));
            achievementLevels.get(i).setBoundary(num);
        }
        adapter.notifyDataSetChanged();
    }

    public static Intent makeIntent(Context context, int poorScore, int greatScore) {
        Intent intent = new Intent(context, PreviewAchievements.class);
        intent.putExtra(EXTRA_POOR_SCORE, poorScore);
        intent.putExtra(EXTRA_GREAT_SCORE, greatScore);
        return intent;
    }

}
