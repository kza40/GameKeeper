package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.myapplication.model.GameManager;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = GameManager.getInstance();

        setupAddButton();
        populateListView();

    }

    private void populateListView() {
        if(gameManager.isEmpty()){
            emptyState(View.VISIBLE);
        } else {
            emptyState(View.INVISIBLE);
        }

        // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.)
//        ListView list = findViewById(R.id.ListGameConfigs);
//        list.setAdapter(adapter)
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