package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;


public class AddConfig extends AppCompatActivity {

    private EditText edtPoorScore;
    private EditText edtGoodScore;
    private EditText edtConfigName;
    private Boolean isEdit;

    private ConfigManager configManager;
    private GameConfig gameConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.addConfigTitle));

        configManager = ConfigManager.getInstance();
        int position=-1;

        setupInputFields();

        if(getIntent().getExtras()!=null)
        {
            isEdit=true;
            position=getIntent().getIntExtra(AddGame.CONFIG_POSITION,-1);
            gameConfig=configManager.getGameConfigAtIndex(position);
            setTitle(getString(R.string.editConfigTitle) + gameConfig.getConfigTitle());
            loadInputFields();
            setupSavedPhoto();
        }
        else {
            isEdit = false;
        }
    }

    private void setupSavedPhoto() {
//        ImageView configPhoto = findViewById(R.id.configImage);
//        configPhoto.setImageResource(gameConfig.getDefaultImage());
//
        TextView tvPhotoCaption = findViewById(R.id.tvPhotoHelp);
        tvPhotoCaption.setText("Photo for " + gameConfig.getConfigTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_game_menu, menu);
        return true;
    }

    private void loadInputFields() {
        edtPoorScore.setText(Integer.toString(gameConfig.getPoorScore()));
        edtGoodScore.setText(Integer.toString(gameConfig.getGoodScore()));
        edtConfigName.setText(gameConfig.getConfigTitle());
    }

    private void setupInputFields() {
        edtPoorScore = findViewById(R.id.poorScore);
        edtGoodScore = findViewById(R.id.goodScore);
        edtConfigName = findViewById(R.id.configName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        else if (id == R.id.settings) {      // need to change id to "save"
            String goodScore = edtGoodScore.getText().toString();
            String poorScore = edtPoorScore.getText().toString();
            String configName = edtConfigName.getText().toString();
            if (!goodScore.isEmpty() && !poorScore.isEmpty() && !configName.isEmpty()) {
                saveConfig();
                onBackPressed();
                return true;
            }
            else {
                Toast.makeText(AddConfig.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveConfig() {
        if(isEdit)
        {
            gameConfig.setGoodScore(Integer.parseInt(edtGoodScore.getText().toString()));
            gameConfig.setPoorScore(Integer.parseInt(edtPoorScore.getText().toString()));
            gameConfig.setConfigTitle(edtConfigName.getText().toString());
        }
        else
        {
            GameConfig gameConfig = new GameConfig(
                    edtConfigName.getText().toString(),
                    Integer.parseInt(edtPoorScore.getText().toString()),
                    Integer.parseInt(edtGoodScore.getText().toString()),
                    "null");
            configManager.addGame(gameConfig);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context,boolean isEdit,int position) {
        Intent intent = new Intent(context, AddConfig.class);
        if(isEdit)
            intent.putExtra(AddGame.CONFIG_POSITION, position);
        return intent;
    }
}
