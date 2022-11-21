package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddGame extends AppCompatActivity {
    public static final String CONFIG_POSITION = "AddGame: Config position";

    private EditText edtScore;
    private EditText edtNumPlayers;
    private ConfigManager configManager;
    private GameConfig gameConfig;

    private TextView tvDifficulty;
    private TextView achievementDisplay;
    private String[] themeTitles;
    private String titleSubLevelOne;
    private String achievementEarned;

    private DifficultyToggle toggle;

    private static final int Tick = 1000;
    private static final int Complete = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_game));

        setupMemberVariables();
        setupSaveButton();
    }

    private void setupMemberVariables() {
        // Config
        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION,-1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        setTitle(getString(R.string.add_game));

        // EditText fields
        edtScore = findViewById(R.id.edtScoreDisplay);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
        edtScore.addTextChangedListener(scoreTextWatcher);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);

        // Achievement-related
        String theme = configManager.getTheme();
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            themeTitles = getResources().getStringArray(R.array.theme_fitness_names);
            titleSubLevelOne = getString(R.string.fitnessLvl0);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            themeTitles = getResources().getStringArray(R.array.theme_spongebob_names);
            titleSubLevelOne = getString(R.string.spongeBobLvl0);
        } else {
            themeTitles = getResources().getStringArray(R.array.theme_starwars_names);
            titleSubLevelOne = getString(R.string.starWarsLvl0);
        }
        achievementDisplay = findViewById(R.id.tvAchievement);

        // Difficulty toggle
        toggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        toggle.setup();
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDifficulty.addTextChangedListener(scoreTextWatcher);
    }

    private TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String scoreInput = edtScore.getText().toString();
            String numPlayersInput = edtNumPlayers.getText().toString();

            if (!scoreInput.isEmpty() && !numPlayersInput.isEmpty()) {
                showAchievement(Integer.parseInt(scoreInput), Integer.parseInt(numPlayersInput));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };


    private void showAchievement(int score, int numPlayers) {
        int index = AchievementCalculator.getScorePlacement(
                themeTitles.length, numPlayers, gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                score, toggle.getScaleFactor());
        String name;
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            name = titleSubLevelOne;
        }
        else {
            name = themeTitles[index];
        }
        achievementEarned = name;
        String message = getString(R.string.you_got) + name + getString(R.string.exclamation);
        achievementDisplay.setText(message);
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {

            String numPlayers = edtNumPlayers.getText().toString();
            String groupScore = edtScore.getText().toString();
            if (!numPlayers.isEmpty() && !groupScore.isEmpty()) {
                saveGame(Integer.parseInt(numPlayers), Integer.parseInt(groupScore));
                celebrate();
                new CountDownTimer(Complete, Tick) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        finish();
                    }
                }.start();



            }
            else {
                Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void celebrate() {
        FragmentManager manager = getSupportFragmentManager();
        CelebrationFragment dialog = new CelebrationFragment();
        dialog.show(manager, "CelebrationFragment");
    }

    private void saveGame(int numPlayers, int groupScore) {
        Game game = new Game(achievementEarned, numPlayers, groupScore, toggle.getScaleFactor());
        gameConfig.addGame(game);
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        return intent;
    }

}
