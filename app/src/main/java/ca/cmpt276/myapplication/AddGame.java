package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION,-1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        setTitle(getString(R.string.add_game));

        titles = new String[] { getString(R.string.achievementZero), getString(R.string.achievementOne),
                                getString(R.string.achievementTwo), getString(R.string.achievementThree),
                                getString(R.string.achievementFour), getString(R.string.achievementFive),
                                getString(R.string.achievementSix), getString(R.string.achievementSeven),
                                getString(R.string.achievementEight) };
        setupEditTextFields();
        setupSaveButton();

        DifficultyToggle toggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        toggle.setup();
    }

    private void setupEditTextFields() {
        edtScore = findViewById(R.id.edtScoreDisplay);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);

        edtScore.addTextChangedListener(scoreTextWatcher);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);
    }

    private TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String scoreInput = edtScore.getText().toString();
            String numPlayersInput = edtNumPlayers.getText().toString();

            if (!scoreInput.isEmpty() && !numPlayersInput.isEmpty()) {
                String temp = AchievementCalculator
                        .getAchievementEarned(titles, Integer.parseInt(numPlayersInput),
                                gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                                Integer.parseInt(scoreInput));
                TextView showAchievement = findViewById(R.id.tvAchievement);
                String message = getString(R.string.you_got) + temp + getString(R.string.exclamation);
                showAchievement.setText(message);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {

            String numPlayers = edtNumPlayers.getText().toString();
            String groupScore = edtScore.getText().toString();
            if (!numPlayers.isEmpty() && !groupScore.isEmpty()) {
                saveGame(Integer.parseInt(numPlayers), Integer.parseInt(groupScore));
                finish();
            }
            else {
                Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void saveGame(int numPlayers, int groupScore) {
        Game game = new Game(titles, numPlayers, groupScore, gameConfig.getPoorScore(), gameConfig.getGoodScore());
        gameConfig.addGame(game);
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        return intent;
    }

}
