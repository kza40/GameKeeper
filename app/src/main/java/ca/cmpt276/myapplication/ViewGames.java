package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.myapplication.adapter.GameAdapter;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;

public class ViewGames extends AppCompatActivity {
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private int configPos;

    private GameAdapter gameAdapter;
    private ListView gamesList;

    private static final String CONFIG_POSITION = "ViewGames: Config position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);

        Intent intent = getIntent();
        configPos = intent.getIntExtra(CONFIG_POSITION,-1);

        gamesList = findViewById(R.id.listOfGames);

        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(gameConfig.getGameTitle() + " Games");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupAddGame();
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameAdapter.notifyDataSetChanged();
        setEmptyState();
    }

    private void populateListView() {
        gameAdapter = new GameAdapter(this, R.layout.game_row, gameConfig.getGames());
        gamesList.setAdapter(gameAdapter);
    }

    private void setupAddGame() {
        FloatingActionButton addGame = findViewById(R.id.btnAddGame);
        addGame.setOnClickListener(view -> {
            Intent intent = AddGame.makeIntent(ViewGames.this, configPos);
            startActivity(intent);
        });
    }

    private void setEmptyState(){
        TextView emptyText = findViewById(R.id.emptyMsg);
        ImageView emptyImg = findViewById(R.id.emptyImg);

        if (gameConfig.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
        }
    }


    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, ViewGames.class);
        intent.putExtra(CONFIG_POSITION,position);
        return intent;
    }

}