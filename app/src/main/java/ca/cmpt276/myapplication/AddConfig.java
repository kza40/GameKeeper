package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.Game;


public class AddConfig extends AppCompatActivity {

    EditText edtPScore;
    EditText edtGScore;
    EditText edtConfigName;

    Button btnPreview;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_config);

        btnPreview = findViewById(R.id.previewBtn);

        setupInputFields();
        setupPreviewButton();
        setupSaveButton();
    }

    private void setupInputFields() {
        edtPScore = findViewById(R.id.poorScore);
        edtGScore = findViewById(R.id.goodScore);
        edtConfigName = findViewById(R.id.configName);

        edtPScore.addTextChangedListener(previewTextWatcher);
        edtGScore.addTextChangedListener(previewTextWatcher);
    }

    private void setupPreviewButton() {
        btnPreview.setOnClickListener(view -> {
            Intent intent = PreviewAchievements.makeIntent(
                        AddConfig.this,
                        Integer.parseInt(edtPScore.getText().toString()),
                        Integer.parseInt(edtGScore.getText().toString())
            );
            startActivity(intent);
        });
    }

    private void setupSaveButton() {
        Button btnSave = findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(view -> {
            game = new Game(edtConfigName.getText().toString(), Integer.parseInt(edtPScore.getText().toString()), Integer.parseInt(edtGScore.getText().toString()));
            finish();
        });
    }

    private TextWatcher previewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pScoreInput = edtPScore.getText().toString();
            String gScoreInput = edtGScore.getText().toString();

            btnPreview.setEnabled(!pScoreInput.isEmpty() && !gScoreInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

}

