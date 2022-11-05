package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewGame extends AppCompatActivity {

    EditText edtScore;
    EditText edtNumPlayers;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


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
}