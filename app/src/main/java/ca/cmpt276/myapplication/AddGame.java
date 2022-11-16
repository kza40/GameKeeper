package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private String[] titles;
    private TextView tvDifficulty;
    private DifficultyToggle toggle;
    private TextView achievementDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION,-1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        setTitle(getString(R.string.add_game));

        setupMemberVariables();
        setupSaveButton();
    }

    private void setupMemberVariables() {
        // EditText fields
        edtScore = findViewById(R.id.edtScoreDisplay);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
        edtScore.addTextChangedListener(scoreTextWatcher);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);

        // Achievement-related
        titles = new String[] { getString(R.string.achievementZero), getString(R.string.achievementOne),
                getString(R.string.achievementTwo), getString(R.string.achievementThree),
                getString(R.string.achievementFour), getString(R.string.achievementFive),
                getString(R.string.achievementSix), getString(R.string.achievementSeven),
                getString(R.string.achievementEight) };
        achievementDisplay = findViewById(R.id.tvAchievement);

        // Difficulty toggle
        toggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        toggle.setup();
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDifficulty.addTextChangedListener(scoreTextWatcher);
    }

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
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
        String name = AchievementCalculator.getAchievementEarned(
                titles, numPlayers, gameConfig.getPoorScore(),
                gameConfig.getGoodScore(), score, toggle.getScaleFactor());

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
                finish();
            } else {
                Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveGame(int numPlayers, int groupScore) {
        Game game = new Game(titles, numPlayers, groupScore, gameConfig.getPoorScore(),
                             gameConfig.getGoodScore(), toggle.getScaleFactor());
        gameConfig.addGame(game);
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        return intent;
    }

}
