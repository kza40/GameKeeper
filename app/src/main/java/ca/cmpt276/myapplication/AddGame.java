package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;

public class AddGame extends AppCompatActivity {

    private EditText edtScore;
    private EditText edtNumPlayers;

    private ConfigManager configManager;
    private GameConfig gameConfig;

    private static final String CONFIG_POSITION = "AddGame: Config position";

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
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            // If fields are not empty
            saveGame();
            finish();
        });
    }

    private void saveGame() {
        Game game = new Game(Integer.parseInt(edtNumPlayers.getText().toString()),
                             Integer.parseInt(edtScore.getText().toString()));
        gameConfig.addGame(game);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        return intent;
    }

}