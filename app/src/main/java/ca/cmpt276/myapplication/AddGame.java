package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddGame extends AppCompatActivity {
    //Constants
    public static final String CONFIG_POSITION = "AddGame: Config position";
    public static final String GAME_POSITION = "AddGame: Game position";
    public static final int MAX_PLAYERS = 25;

    //UI Variables
    private TextView tvTotalScore;
    private EditText edtNumPlayers;
    private EditText[] edtIndividualScore;
    private DifficultyToggle difficultyToggle;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private int NUM_ROWS = 0;
    private Boolean isEdit;
    private int totalScore;
    private boolean isFilled;

    private String[] themeTitles;
    private String titleSubLevelOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.add_game_title));

        setupGameObjects();
        loadCurrentTheme();
        setupUIElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_game_menu, menu);
        return true;
    }

    private void setupGameObjects() {
        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION, -1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        isEdit=false;
        isFilled = false;

        if (intent.getIntExtra(GAME_POSITION, -1) != -1)
        {
            setTitle(getString(R.string.edit_game_title));
            isFilled = true;
            isEdit=true;
            int gamePos=intent.getIntExtra(GAME_POSITION, -1);
            currentGame=gameConfig.getGameAtIndex(gamePos);
        }
    }

    private void loadCurrentTheme() {
        String theme = configManager.getTheme();
        if (theme.equals(ThemeSetting.THEME_FITNESS)) {
            themeTitles = getResources().getStringArray(R.array.theme_fitness_names);
            titleSubLevelOne = getString(R.string.fitnessLvl0);
        } else if (theme.equals(ThemeSetting.THEME_SPONGEBOB)) {
            themeTitles = getResources().getStringArray(R.array.theme_spongebob_names);
            titleSubLevelOne = getString(R.string.spongeBobLvl0);
        } else {
            themeTitles = getResources().getStringArray(R.array.theme_starwars_names);
            titleSubLevelOne = getString(R.string.starWarsLvl0);
        }
    }

    private void setupUIElements() {
        tvTotalScore = findViewById(R.id.tvTotalScore);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
        setupDifficultyToggle();
        populateEdittextScores(isEdit);
        if(isEdit)
        {
            totalScore = currentGame.getGroupScore();
            tvTotalScore.setText(getString(R.string.score_colon) + totalScore);
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
            edtNumPlayers.setText(Integer.toString(currentGame.getNumOfPlayers()));
        }
        else
        {
            tvTotalScore.setText(R.string.score_equals_zero);
        }
        edtNumPlayers.addTextChangedListener(playerNumTextWatcher);
    }

    private void setupDifficultyToggle() {
        difficultyToggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        difficultyToggle.setup();
    }

    private void populateEdittextScores(Boolean isEdit) {
        if(isEdit) {
            NUM_ROWS = currentGame.getNumOfPlayers();
            edtIndividualScore = new EditText[NUM_ROWS];
        }
        LinearLayout table = findViewById(R.id.LayoutForEdittexts);
        table.removeAllViews();

        for (int row = 0; row < NUM_ROWS; row++) {
            EditText editText = setupEditText(row);
            LinearLayout.LayoutParams lp = setupLinearLayoutParameters();

            if(isEdit) {
                editText.setText(currentGame.getPlayerScoresAtIndex(row));
            }
            table.addView(editText, lp);
            edtIndividualScore[row] = editText;
            edtIndividualScore[row].addTextChangedListener(scoreTextWatcher);
        }
    }


    private final TextWatcher playerNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numPlayersInput = edtNumPlayers.getText().toString();
            totalScore = 0;
            if (!numPlayersInput.isEmpty()) {
                NUM_ROWS = Integer.parseInt(numPlayersInput);
                if (NUM_ROWS > MAX_PLAYERS) {
                    Toast.makeText(getApplicationContext(), R.string.input_less_message, Toast.LENGTH_SHORT).show();
                    edtNumPlayers.setText("");
                } else {
                    edtIndividualScore = new EditText[NUM_ROWS];
                    populateEdittextScores(false);
                }
            } else {
                LinearLayout table = findViewById(R.id.LayoutForEdittexts);
                table.removeAllViews();
                isFilled = false;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int numFilled = 0;
            totalScore = 0;

            for (int row = 0; row < NUM_ROWS; row++) {
                String currField = edtIndividualScore[row].getText().toString();
                if (!currField.isEmpty()) {
                    totalScore += Integer.parseInt(currField);
                    numFilled++;
                }
            }

            if (numFilled == NUM_ROWS) {
                isFilled = true;      // ready for save!
            } else {
                isFilled = false;
            }
            tvTotalScore.setText(Integer.toString(totalScore));
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isFilled) {
            int numPlayers = Integer.parseInt(edtNumPlayers.getText().toString());
            String achievementEarned = getAchievementName(totalScore, numPlayers);

            saveGame(achievementEarned, numPlayers);
            celebrate(achievementEarned);
        } else {
            Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private String getAchievementName(int score, int numPlayers) {
        int index = AchievementCalculator.getScorePlacement(
                themeTitles.length, numPlayers, gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                score, difficultyToggle.getScaleFactor());
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            return titleSubLevelOne;
        } else {
            return themeTitles[index];
        }
    }

    private void saveGame(String achievementEarned, int numPlayers) {
        String[] scores = new String[NUM_ROWS];
        for (int i = 0; i < NUM_ROWS; i++) {
            scores[i] = edtIndividualScore[i].getText().toString();
        }

        if(isEdit)
        {
            currentGame.setAchievementEarned(achievementEarned);
            currentGame.setNumOfPlayers(numPlayers);
            currentGame.setGroupScore(totalScore);
            currentGame.setScaleFactor(difficultyToggle.getScaleFactor());
            currentGame.setPlayerScores(scores);
        }
        else
        {
            Game game = new Game(achievementEarned, numPlayers, totalScore,
                                 difficultyToggle.getScaleFactor(), scores);
            gameConfig.addGame(game);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    private void celebrate(String achievementEarned) {
        Intent intent = CelebrationPage.makeIntent(this, achievementEarned);
        startActivity(intent);
    }

    private LinearLayout.LayoutParams setupLinearLayoutParameters() {
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); // Verbose!
        lp.weight = 1.0f; // This is critical. Doesn't work without it.
        lp.setMargins(240, 10, 240, 10);
        return lp;
    }

    private EditText setupEditText(int row) {
        EditText editText=new EditText(getApplicationContext());
        editText.setHint(getString(R.string.score_num) + (row + 1));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        return editText;
    }

    public static Intent makeIntent(Context context,boolean isEdit, int position,int gamePosition) {
        Intent intent = new Intent(context, AddGame.class);
        intent.putExtra(CONFIG_POSITION, position);
        if(isEdit) {
            intent.putExtra(GAME_POSITION, gamePosition);
        }
        return intent;
    }
}
