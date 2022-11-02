package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGame extends AppCompatActivity {

    EditText edtP1Score;
    EditText edtP2Score;
    EditText edtPScore;
    EditText edtGScore;
    EditText edtGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);




        setupInputFields();
        setupSaveButton();
    }

    private void setupInputFields() {
        edtP1Score = findViewById(R.id.p1Score);
        edtP2Score = findViewById(R.id.p2Score);
        edtPScore = findViewById(R.id.poorScore);
        edtGScore = findViewById(R.id.goodScore);
        edtGameName = findViewById(R.id.gameName);

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