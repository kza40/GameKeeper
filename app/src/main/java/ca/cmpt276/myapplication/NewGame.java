package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.GameManager;

public class NewGame extends AppCompatActivity {

    EditText edtScore;
    EditText edtNumPlayers;
    Button btnSave;
    GameManager gameManager;
    private static final String POSITION_VALUE ="POSITION_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        Intent i=getIntent();
        int pos=i.getIntExtra(POSITION_VALUE,-1);
        //You can now get the value of Game config from GameManager class as you have the position of the

        gameManager = GameManager.getInstance();
        setTitle(gameManager.getGameConfigs().get(pos).getGameTitle());


        setupEditTextFields();
        setupSaveButton();

    }

    private void setupSaveButton() {
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            //this is just for now
            Toast.makeText(NewGame.this, "Just a demo", Toast.LENGTH_LONG).show();
        });
    }

    private void setupEditTextFields() {
        edtScore = findViewById(R.id.edtScoreDisplay);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
    }

    public static Intent makeIntent(Context context,int position) {
        Intent intent = new Intent(context, NewGame.class);
        intent.putExtra(POSITION_VALUE,position);
        return intent;
    }
}