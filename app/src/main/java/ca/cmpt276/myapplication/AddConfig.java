package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;


public class AddConfig extends AppCompatActivity {

    private EditText edtPoorScore;
    private EditText edtGoodScore;
    private EditText edtConfigName;
    private Button btnPreview;

    private ConfigManager configManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_config);
        setTitle("Add Config");

        btnPreview = findViewById(R.id.previewBtn);
        configManager = ConfigManager.getInstance();

        setupInputFields();
        setupPreviewButton();
        setupSaveButton();
    }

    private void setupInputFields() {
        edtPoorScore = findViewById(R.id.poorScore);
        edtGoodScore = findViewById(R.id.goodScore);
        edtConfigName = findViewById(R.id.configName);

        edtPoorScore.addTextChangedListener(previewTextWatcher);
        edtGoodScore.addTextChangedListener(previewTextWatcher);
    }

    private void setupPreviewButton() {
        btnPreview.setOnClickListener(view -> {
            Intent intent = PreviewAchievements.makeIntent(
                        AddConfig.this,
                        Integer.parseInt(edtPoorScore.getText().toString()),
                        Integer.parseInt(edtGoodScore.getText().toString())
            );
            startActivity(intent);
        });
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(view -> {
            // If good/poor score fields are not empty
            saveConfig();
            finish();
        });
    }

    private void storeGameConfigToSharedPreferences(ConfigManager configManager) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        for(int i = 0; i < configManager.size(); i++) {
            prefs = this.getSharedPreferences("Game Configs" + (i+1), MODE_PRIVATE);
            editor = prefs.edit();
            //editor.clear(); //clears stored data in SharedPreferences(resets to default), comment all editor.puts and uncomment this to clear
            editor.putString("Game Title" + (i+1), configManager.getGameConfigAtIndex(i).getGameTitle());
            editor.putInt("Poor Score" + (i+1), configManager.getGameConfigAtIndex(i).getPoorScore());
            editor.putInt("Good Score" + (i+1), configManager.getGameConfigAtIndex(i).getGoodScore());
            editor.apply();
        }
        prefs = this.getSharedPreferences("Number of Games", MODE_PRIVATE);
        editor = prefs.edit();
        //editor.clear(); //clears stored data in SharedPreferences(resets to default), comment all editor.puts and uncomment this to clear
        editor.putInt("ConfigManager Size", configManager.size());
        editor.apply();
    }

    static public GameConfig getGameConfigFromSharedPreferences(Context context, int i) {
        String gameTitle;
        int poorScore;
        int goodScore;
        SharedPreferences prefs;
        prefs = context.getSharedPreferences("Game Configs" + (i+1), MODE_PRIVATE);
        gameTitle = prefs.getString("Game Title" + (i+1), "This is empty");
        poorScore = prefs.getInt("Poor Score" + (i+1), -1);
        goodScore = prefs.getInt("Good Score" + (i+i), -1);
        GameConfig gameConfig = new GameConfig(gameTitle, poorScore, goodScore);
        return gameConfig;
    }

    static public int getCountFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Number of Games", MODE_PRIVATE);
        return prefs.getInt("ConfigManager Size", 0);
    }

    private void saveConfig() {
        GameConfig gameConfig = new GameConfig(
                edtConfigName.getText().toString(),
                Integer.parseInt(edtPoorScore.getText().toString()),
                Integer.parseInt(edtGoodScore.getText().toString())
        );
        configManager.addGame(gameConfig);
        storeGameConfigToSharedPreferences(configManager);
    }

    private TextWatcher previewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pScoreInput = edtPoorScore.getText().toString();
            String gScoreInput = edtGoodScore.getText().toString();

            btnPreview.setEnabled(!pScoreInput.isEmpty() && !gScoreInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, AddConfig.class);
        return intent;
    }
}

