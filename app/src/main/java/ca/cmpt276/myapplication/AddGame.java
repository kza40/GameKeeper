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

import java.util.List;

import ca.cmpt276.myapplication.model.Achievements;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddGame extends AppCompatActivity {
    private EditText edtScore;
    private EditText edtNumPlayers;
    private String[] titles = new String[] { "Lvl 0", "Lvl 1", "Lvl 2", "Lvl 3", "Lvl 4", "Lvl 5",
            "Lvl 6", "Lvl 7", "Lvl 8" };

    private ConfigManager configManager;
    private GameConfig gameConfig;

    public static final String CONFIG_POSITION = "AddGame: Config position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION,-1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        setTitle("Add Game");

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

            if(!scoreInput.isEmpty() && !numPlayersInput.isEmpty()) {
                String temp = Achievements
                        .getAchievementEarned(titles, Integer.parseInt(numPlayersInput),
                                gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                                Integer.parseInt(scoreInput));
                TextView showAchievement = findViewById(R.id.tvAchievement);
                showAchievement.setText("You got " + temp + "!");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            //TO DO: If fields are not empty
            saveGame();
            finish();
        });
    }

    private void saveGame() {
        int numPlayers = Integer.parseInt(edtNumPlayers.getText().toString());
        int groupScore = Integer.parseInt(edtScore.getText().toString());
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