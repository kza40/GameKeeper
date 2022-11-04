package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class theGames extends AppCompatActivity {
//this is where the list of Games should show when you click on a gameConfig
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_games);
    }
}