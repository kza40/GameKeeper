package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Game Configs");
        gameManager = GameManager.getInstance();

        setupAddButton();
        populateListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView() {
        if(gameManager.isEmpty()){
            emptyState(View.VISIBLE);
        } else {
            emptyState(View.INVISIBLE);
        }
        //just strings in this list
        List<String> theConfigs = new ArrayList<>();
        for(GameConfig gameConfig : gameManager){
            theConfigs.add(gameConfig.getGameTitle()+"\n");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.configs_layout, theConfigs);
        ListView list = findViewById(R.id.listOfConfigs);
        list.setAdapter(adapter);
    }



    private void setupAddButton() {
        ImageButton add = findViewById(R.id.addButton);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddConfig.class);
            startActivity(intent);
        });
    }

    private void emptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMessage);
        emptyText.setVisibility(visible);
    }
}