package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;


public class AddConfig extends AppCompatActivity {

    private EditText edtPoorScore;
    private EditText edtGoodScore;
    private EditText edtConfigName;
    private Boolean isEdit;

    private ConfigManager configManager;
    GameConfig gameConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.addConfigTitle));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configManager = ConfigManager.getInstance();
        int position=-1;

        setupInputFields();
        setupSaveButton();

        if(getIntent().getExtras()!=null)
        {
            isEdit=true;
            getSupportActionBar().setTitle(getString(R.string.editConfigTitle));
            position=getIntent().getIntExtra(AddGame.CONFIG_POSITION,-1);
            loadInputFields(position);
        }
        else
            isEdit=false;
    }

    private void loadInputFields(int position) {
        gameConfig=configManager.getGameConfigAtIndex(position);
        edtPoorScore.setText(Integer.toString(gameConfig.getPoorScore()));
        edtGoodScore.setText(Integer.toString(gameConfig.getGoodScore()));
        edtConfigName.setText(gameConfig.getGameTitle());

    }

    private void setupInputFields() {
        edtPoorScore = findViewById(R.id.poorScore);
        edtGoodScore = findViewById(R.id.goodScore);
        edtConfigName = findViewById(R.id.configName);
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(view -> {

            String goodScore = edtGoodScore.getText().toString();
            String poorScore = edtPoorScore.getText().toString();
            String configName = edtConfigName.getText().toString();
            if (!goodScore.isEmpty() && !poorScore.isEmpty() && !configName.isEmpty()) {
                saveConfig();
                finish();
            }
            else {
                Toast.makeText(AddConfig.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    private void saveConfig() {
        if(isEdit)
        {
            gameConfig.setGoodScore(Integer.parseInt(edtGoodScore.getText().toString()));
            gameConfig.setPoorScore(Integer.parseInt(edtPoorScore.getText().toString()));
            gameConfig.setGameTitle(edtConfigName.getText().toString());
        }
        else
        {
            GameConfig gameConfig = new GameConfig(
                    edtConfigName.getText().toString(),
                    Integer.parseInt(edtPoorScore.getText().toString()),
                    Integer.parseInt(edtGoodScore.getText().toString())
            );
            configManager.addGame(gameConfig);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context,boolean isEdit,int position) {
        Intent intent = new Intent(context, AddConfig.class);
        if(isEdit)
            intent.putExtra(AddGame.CONFIG_POSITION, position);
        return intent;
    }
}

