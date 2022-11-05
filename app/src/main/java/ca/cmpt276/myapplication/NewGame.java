package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.GameManager;

public class NewGame extends AppCompatActivity {

    EditText edtScore;
    EditText edtNumPlayers;
    Button btnSave;
    private GameManager gameManager;
    private GameConfig gameConfig;
    private Game game;

    private static final String POSITION_VALUE ="POSITION_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Intent i=getIntent();
        int pos=i.getIntExtra(POSITION_VALUE,-1);

        gameManager = GameManager.getInstance();
        gameConfig=gameManager.getGameConfigs().get(pos);
        setTitle(gameConfig.getGameTitle());

        setupEditTextFields();
        setupSaveButton();


    }

    private void setupSaveButton() {
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            //this is just for now
            Toast.makeText(NewGame.this, "Just a demo", Toast.LENGTH_LONG).show();
            game = new Game(Integer.parseInt(edtNumPlayers.getText().toString()), Integer.parseInt(edtScore.getText().toString()));
            gameConfig.addGame(game);
            finish();


        });

    }

    private void setupEditTextFields() {
        edtScore = findViewById(R.id.edtScoreDisplay);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
    }

}