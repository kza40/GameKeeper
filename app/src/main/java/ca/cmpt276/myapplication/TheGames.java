package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.GameManager;

public class TheGames extends AppCompatActivity {
    private GameManager gameManager;
    private GameConfig gameConfig;

    private static final String POSITION_VALUE ="POSITION_VALUE";
//this is where the list of Games should show when you click on a gameConfig
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_games);

        Intent i=getIntent();
        int pos=i.getIntExtra(POSITION_VALUE,-1);
        //You can now get the value of Game config from GameManager class as you have the position of the

        gameManager = GameManager.getInstance();
        gameConfig=gameManager.getGameConfigs().get(pos);
        setTitle(gameConfig.getGameTitle());


        setupAddButton();
        populateListView();
    }



    private void populateListView() {
        if(gameConfig.isEmpty()){
            emptyState(View.VISIBLE);
        } else {
            emptyState(View.INVISIBLE);
        }

        List<String> theGames = new ArrayList<>();
        for(Game game : gameConfig){
            theGames.add(game.getNumOfPlayers()+"\n"+game.getScore()+"\n");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.configs_layout, theGames);
        ListView list = findViewById(R.id.listOfGames);
        list.setAdapter(adapter);
    }

    private void setupAddButton() {
        ImageButton add = findViewById(R.id.addBtn);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(TheGames.this, NewGame.class);
            startActivity(intent);
        });
    }

    private void emptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMsg);
        ImageView emptyImg = findViewById(R.id.emptyImg);
        emptyText.setVisibility(visible);
        emptyImg.setVisibility(visible);
    }


    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, TheGames.class);
        intent.putExtra(POSITION_VALUE,position);
        return intent;
    }

}