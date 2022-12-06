package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class ViewConfigs extends AppCompatActivity {

    private ConfigManager configManager;
    private ListView configsList;
    private GameConfigAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_configs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.viewConfigsTitle));

        configManager=new SharedPreferenceManager(getApplicationContext()).getConfigManager();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_configs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = ThemeSetting.makeIntent(this);
                startActivity(intent);
                return true;

            case R.id.about:
                intent = AboutScreen.makeIntent(this);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateListView() {
            List<String> theConfigs = new ArrayList<>();
            for (GameConfig gameConfig : configManager) {
                theConfigs.add(gameConfig.getConfigTitle());
            }
            adapter=new GameConfigAdapter(ViewConfigs.this,R.layout.config_row,configManager.getGameConfigs());
            configsList.setAdapter(adapter);

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
        if (configManager.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.VISIBLE);
        }
        else {
            emptyText.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
        }
    }
}