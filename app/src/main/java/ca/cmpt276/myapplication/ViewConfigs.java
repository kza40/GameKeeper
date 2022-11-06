package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;

public class ViewConfigs extends AppCompatActivity {

    private ConfigManager configManager;
    private ListView configsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Game Configs");

        configManager = ConfigManager.getInstance();
        configsList = findViewById(R.id.listOfConfigs);

        populateListView();
        setupAddConfig();
        setupViewGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView() {
        if (configManager.isEmpty()) {
            setEmptyState(View.VISIBLE);
        } else {
            setEmptyState(View.INVISIBLE);

            List<String> theConfigs = new ArrayList<>();
            for (GameConfig gameConfig : configManager) {
                theConfigs.add(gameConfig.getGameTitle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.configs_layout, theConfigs);
            configsList.setAdapter(adapter);
        }
    }

    private void setupAddConfig() {
        FloatingActionButton addConfig = findViewById(R.id.btnAddConfig);
        addConfig.setOnClickListener(view -> {
            Intent intent = AddConfig.makeIntent(ViewConfigs.this);
            startActivity(intent);
        });
    }

    private void setupViewGames() {
        configsList.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent intent = ViewGames.makeIntent(ViewConfigs.this, position);
            startActivity(intent);
        });
    }

    private void setEmptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMessage);
        ImageView emptyImg = findViewById(R.id.emptyStateImg);
        emptyText.setVisibility(visible);
        emptyImg.setVisibility(visible);
    }
}