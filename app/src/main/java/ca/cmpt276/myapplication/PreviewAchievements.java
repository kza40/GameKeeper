package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.adapter.AchievementAdapter;
import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.ui_features.AchievementManager;
import ca.cmpt276.myapplication.ui_features.DifficultyToggle;

public class PreviewAchievements extends AppCompatActivity {
    private static final String EXTRA_POOR_SCORE = "ca.cmpt276.myapplication: poor score";
    private static final String EXTRA_GREAT_SCORE = "ca.cmpt276.myapplication: great score";
    private static final int NUM_ACHIEVEMENTS = AchievementManager.NUMBER_OF_ACHIEVEMENT_POS - 1;

    private int poorScore;
    private int greatScore;

    private EditText edtNumPlayers;
    private ListView achievementsList;
    private AchievementAdapter adapter;

    private AchievementManager achievementManager;
    private List<AchievementLevel> achievementLevels;
    private DifficultyToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_achievements);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.achievementsTitle));

        setUpMemberVariables();
        setupAchievementLevels();
    }

    private void setUpMemberVariables() {
        Intent intent = getIntent();
        poorScore = intent.getIntExtra(EXTRA_POOR_SCORE, 0);
        greatScore = intent.getIntExtra(EXTRA_GREAT_SCORE, 0);

        // EditText field
        edtNumPlayers = findViewById(R.id.edtNumPlayers);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);
        achievementsList = findViewById(R.id.achievementLevels);

        View view = findViewById(android.R.id.content).getRootView();
        // Achievement levels
        achievementManager = new AchievementManager(view, ConfigManager.getInstance().getTheme());
        achievementLevels = new ArrayList<>();

        // Difficulty toggle
        toggle = new DifficultyToggle(view);
        toggle.setup();
        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDifficulty.addTextChangedListener(scoreTextWatcher);
    }

    private void setupAchievementLevels() {
        for (int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            String title = achievementManager.getAchievementAtIndex(i + 1);
            AchievementLevel newLevel = new AchievementLevel(title);
            achievementLevels.add(newLevel);
        }
        adapter = new AchievementAdapter(this, R.layout.achievement_preview_row, achievementLevels);
        achievementsList.setAdapter(adapter);
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
        List<Integer> boundaries = AchievementCalculator
                                    .getBoundaries(NUM_ACHIEVEMENTS, numPlayers, poorScore,
                                    greatScore, toggle.getScaleFactor());
        for(int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            String value = Integer.toString(boundaries.get(i));
            achievementLevels.get(i).setBoundary(value);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context, int poorScore, int greatScore) {
        Intent intent = new Intent(context, PreviewAchievements.class);
        intent.putExtra(EXTRA_POOR_SCORE, poorScore);
        intent.putExtra(EXTRA_GREAT_SCORE, greatScore);
        return intent;
    }
}
