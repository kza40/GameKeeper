package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.GameConfig;

public class theGames extends AppCompatActivity {
    private GameConfig gameConfig;
//this is where the list of Games should show when you click on a gameConfig
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_games);

        //gotta retrieve the clicked on gameConfig somehow from MainActivity when its clicked

        setUpSaveButton();
        populateListView();
    }

    private void setUpSaveButton() {

    }

    private void populateListView() {



    }

    private void emptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMessage);
        emptyText.setVisibility(visible);
    }

}