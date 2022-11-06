package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;

public class ViewGames extends AppCompatActivity {
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private int configPos;

    private static final String CONFIG_POSITION = "ViewGames: Config position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);

        Intent intent = getIntent();
        configPos = intent.getIntExtra(CONFIG_POSITION,-1);

        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        setTitle(gameConfig.getGameTitle());

        setupAddGame();
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView() {
        if (gameConfig.isEmpty()) {
            setEmptyState(View.VISIBLE);
        } else {
            setEmptyState(View.INVISIBLE);
            List<String> theGames = new ArrayList<>();

            for (Game game : gameConfig) {
                theGames.add(game.getNumOfPlayers() + "\n" + game.getScore() + "\n");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.configs_layout, theGames);
            ListView list = findViewById(R.id.listOfGames);
            list.setAdapter(adapter);
        }
    }

    private void setupAddGame() {
        FloatingActionButton addGame = findViewById(R.id.btnAddGame);
        addGame.setOnClickListener(view -> {
            Intent intent = AddGame.makeIntent(ViewGames.this, configPos);
            startActivity(intent);
        });
    }

    private void setEmptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMsg);
        ImageView emptyImg = findViewById(R.id.emptyImg);
        emptyText.setVisibility(visible);
        emptyImg.setVisibility(visible);
    }

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, ViewGames.class);
        intent.putExtra(CONFIG_POSITION,position);
        return intent;
    }

}