package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
    private Boolean isEdit;

    private ConfigManager configManager;
    GameConfig gameConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_config);
        setTitle("Add Config");
        btnPreview = findViewById(R.id.previewBtn);
        configManager = ConfigManager.getInstance();
        int position=-1;

        setupInputFields();
        setupPreviewButton();
        setupSaveButton();

        if(getIntent().getExtras()!=null)
        {
            isEdit=true;
            setTitle("Edit Config");
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
            //TO DO: If good/poor score fields are not empty
            saveConfig();
            finish();
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

    public static Intent makeIntent(Context context,boolean isEdit,int position) {
        Intent intent = new Intent(context, AddConfig.class);
        if(isEdit)
            intent.putExtra(AddGame.CONFIG_POSITION, position);
        return intent;
    }
}

