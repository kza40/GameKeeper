package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    private String[] starWarsTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION,-1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_game));

        starWarsTitles = new String[] { getString(R.string.starWarsLvl1), getString(R.string.starWarsLvl2), getString(R.string.starWarsLvl3),
                getString(R.string.starWarsLvl4), getString(R.string.starWarsLvl5), getString(R.string.starWarsLvl6),
                getString(R.string.starWarsLvl7), getString(R.string.starWarsLvl8)};
        setupEditTextFields();
        setupSaveButton();
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
                        .getAchievementEarned(starWarsTitles, Integer.parseInt(numPlayersInput),
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
        Game game = new Game(starWarsTitles, numPlayers, groupScore, gameConfig.getPoorScore(), gameConfig.getGoodScore());
        gameConfig.addGame(game);
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        return intent;
    }

}
