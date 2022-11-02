package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAddButton();
        populateListView();

    }

    private void populateListView() {
        //need an if empty conditional statement here
        //if no games -> emptyState(View.Visible);
        //else -> emptyState(View.inVisible);
    }

    private void setupAddButton() {
        ImageButton add = findViewById(R.id.addButton);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddGame.class);
            startActivity(intent);
        });
    }

    private void emptyState(int visible){
        TextView emptyText = findViewById(R.id.emptyMessage);
        emptyText.setVisibility(visible);
    }
}