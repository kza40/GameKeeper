package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGame extends AppCompatActivity {

    EditText example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);



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