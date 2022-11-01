package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGame extends AppCompatActivity {

    EditText p1Score;
    EditText p2Score;
    EditText pScore;
    EditText gScore;
    EditText gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);


        p1Score = findViewById(R.id.p1Score);
        p2Score = findViewById(R.id.p2Score);
        pScore = findViewById(R.id.poorScore);
        gScore = findViewById(R.id.goodScore);
        gameName = findViewById(R.id.gameName);

        setupSaveButton();
    }

    private void setupSaveButton() {
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(view -> {
            //this is just for now
            Toast.makeText(AddGame.this, "Just a demo", Toast.LENGTH_LONG).show();
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddGame.class);
    }
}