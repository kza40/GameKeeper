package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.adapter.GameConfigAdapter;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;

public class ViewConfigs extends AppCompatActivity {

    private ConfigManager configManager;
    private ListView configsList;
    private GameConfigAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Game Configs");

        configManager = ConfigManager.getInstance();
        configsList = findViewById(R.id.listOfConfigs);

        setEmptyState();
        populateListView();
        setupAddConfig();
        setupViewGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        setEmptyState();
    }


    private void populateListView() {
        if (configManager.isEmpty()) {
            restoreGameConfigsFromSharedPreferences();
        }
            List<String> theConfigs = new ArrayList<>();
            for (GameConfig gameConfig : configManager) {
                theConfigs.add(gameConfig.getGameTitle());
            }
            adapter=new GameConfigAdapter(ViewConfigs.this,R.layout.adapter_view,configManager.getGameConfigs());
            configsList.setAdapter(adapter);

    }

    private void restoreGameConfigsFromSharedPreferences() {
        for(int i = 0; i < AddConfig.getCountFromSharedPreferences(this); i++) {
            configManager.addGame(AddConfig.getGameConfigFromSharedPreferences(this, i));
        }
    }

    private void setupAddConfig() {
        FloatingActionButton addConfig = findViewById(R.id.btnAddConfig);
        addConfig.setOnClickListener(view -> {
            Intent intent = AddConfig.makeIntent(ViewConfigs.this,false,-1);
            startActivity(intent);
        });
    }

    private void setupViewGames() {
        configsList.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent intent = ViewGames.makeIntent(ViewConfigs.this, position);
            startActivity(intent);
        });
    }

    public void setEmptyState(){
        TextView emptyText = findViewById(R.id.emptyMessage);
        ImageView emptyImg = findViewById(R.id.emptyStateImg);
        if (AddConfig.getCountFromSharedPreferences(this) == 0) {
            emptyText.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.VISIBLE);
        }
        else {
            emptyText.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
        }
    }
}