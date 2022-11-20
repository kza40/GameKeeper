package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.adapter.AchievementAdapter;
import ca.cmpt276.myapplication.model.AchievementLevel;
import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.ConfigManager;

public class PreviewAchievements extends AppCompatActivity {
    private static final String EXTRA_POOR_SCORE = "ca.cmpt276.myapplication: poor score";
    private static final String EXTRA_GREAT_SCORE = "ca.cmpt276.myapplication: great score";
    private static final int NUM_ACHIEVEMENTS = 8;

    private int poorScore;
    private int greatScore;
    private int choosenTheme;
    private EditText edtNumPlayers;
    private ListView list;
    private AchievementAdapter adapter;


    private String[] starWarsTitles;
    private String[] fitnessTitles;
    private String[] spongeBobTitles;
    private List<AchievementLevel> achievementLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_achievements);


        setTitle(R.string.achievementsTitle);
        setUpMemberVariables();
        setupAchievementLevels(ConfigManager.getInstance().getTheme());
        setupSettingBtn();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        achievementLevels.clear();
        setupAchievementLevels(ConfigManager.getInstance().getTheme());
        adapter.notifyDataSetChanged();
    }

    private void setupSettingBtn() {
        Button settingBtn = findViewById(R.id.btnSetting);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = ThemeSetting.makeIntent(PreviewAchievements.this);
                startActivity(intent);
            }
        });
    }

    private void setUpMemberVariables() {
        Intent intent = getIntent();
        poorScore = intent.getIntExtra(EXTRA_POOR_SCORE, 0);
        greatScore = intent.getIntExtra(EXTRA_GREAT_SCORE, 0);
        edtNumPlayers = findViewById(R.id.edtNumPlayers);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);
        list = findViewById(R.id.achievementLevels);

        switch(choosenTheme) {
            case 0:
                fitnessTitles = new String[]{getString(R.string.fitnessLvl1), getString(R.string.fitnessLvl2),
                        getString(R.string.fitnessLvl3), getString(R.string.fitnessLvl4), getString(R.string.fitnessLvl5),
                        getString(R.string.fitnessLvl6), getString(R.string.fitnessLvl7), getString(R.string.fitnessLvl8)};
                break;
            case 1:
                spongeBobTitles = new String[]{getString(R.string.spongeBobLvl1), getString(R.string.spongeBobLvl2), getString(R.string.spongeBobLvl3),
                        getString(R.string.spongeBobLvl4), getString(R.string.spongeBobLvl5), getString(R.string.spongeBobLvl6),
                        getString(R.string.spongeBobLvl7), getString(R.string.spongeBobLvl8)};
                break;
            default:
            starWarsTitles = new String[]{getString(R.string.starWarsLvl1), getString(R.string.starWarsLvl2), getString(R.string.starWarsLvl3),
                    getString(R.string.starWarsLvl4), getString(R.string.starWarsLvl5), getString(R.string.starWarsLvl6),
                    getString(R.string.starWarsLvl7), getString(R.string.starWarsLvl8)};
        }
        achievementLevels = new ArrayList<>();
    }

    private void setupAchievementLevels(String theme) {
        String[] themeTitles;
        if(theme.equals(ThemeSetting.THEME_FITNESS))
            themeTitles=fitnessTitles;
        else if(theme.equals(ThemeSetting.THEME_SPONGEBOB))
            themeTitles=spongeBobTitles;
        else
            themeTitles=starWarsTitles;

        for(int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            AchievementLevel newLevel = new AchievementLevel(themeTitles[i]);
            achievementLevels.add(newLevel);
        }
        adapter = new AchievementAdapter(this, R.layout.adapter_view3, achievementLevels);
        list.setAdapter(adapter);
    }

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numPlayersInput = edtNumPlayers.getText().toString();
            if(!numPlayersInput.isEmpty()) {
                updateListView(Integer.parseInt(numPlayersInput));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void updateListView(int numPlayers) {
        List<Integer> boundaries = AchievementCalculator.getBoundaries(numPlayers, poorScore, greatScore);
        for(int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            String value = Integer.toString(boundaries.get(i));
            achievementLevels.get(i).setBoundary(value);
        }
        adapter.notifyDataSetChanged();
    }

    public static Intent makeIntent(Context context, int poorScore, int greatScore) {
        Intent intent = new Intent(context, PreviewAchievements.class);
        intent.putExtra(EXTRA_POOR_SCORE, poorScore);
        intent.putExtra(EXTRA_GREAT_SCORE, greatScore);
        return intent;
    }
}
