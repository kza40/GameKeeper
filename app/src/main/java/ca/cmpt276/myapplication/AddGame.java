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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddGame extends AppCompatActivity {
    //Constants
    public static final String CONFIG_POSITION = "AddGame: Config position";
    public static final String GAME_POSITION = "AddGame: Game position";
    private static final int TICK = 1000;
    private static final int COMPLETE = 10000;
    public static final int MAX_PLAYERS = 25;
    public static final String CELEBRATION_FRAGMENT = "CelebrationFragment";

    //UI Variables
    private TextView tvTotalScore;
    private EditText edtPlayerCount;
    private DifficultyToggle difficultyToggle;
    private LinearLayout table;
    private LinearLayout.LayoutParams lp;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private boolean isEdit;
    private int totalScore;

    private List<EditText> playerScores;
    private boolean isFilled;
    private int numActiveFields;

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
        isEdit = false;

        if (intent.getIntExtra(GAME_POSITION, -1) != -1) {
            setTitle(getString(R.string.edit_game_title));
            isEdit = true;
            int gamePos = intent.getIntExtra(GAME_POSITION, -1);
            currentGame = gameConfig.getGameAtIndex(gamePos);
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
        edtPlayerCount = findViewById(R.id.edtNumPlayersDisplay);
        setupDifficultyToggle();

        if(isEdit) {
            totalScore = currentGame.getGroupScore();
            String total = getString(R.string.score_colon) + totalScore;
            tvTotalScore.setText(total);

            String count = Integer.toString(currentGame.getNumOfPlayers());
            edtPlayerCount.setText(count);

            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
        } else {
            totalScore = 0;
            tvTotalScore.setText(R.string.score_equals_zero);
            edtPlayerCount.setText(R.string.default_player_count);
        }

        numActiveFields = Integer.parseInt(edtPlayerCount.getText().toString());

        loadPlayerScores();
        updateScoreFields();

        edtPlayerCount.addTextChangedListener(playerNumTextWatcher);
        isFilled = true;
    }

    private void loadPlayerScores() {
        playerScores = new ArrayList<>();

        for (int i = 0; i < numActiveFields; i++) {
            EditText scoreField;
            if (isEdit) {
                scoreField = setupEditText("" + currentGame.getPlayerScoresAtIndex(i), i);
            }
            else {
                scoreField = setupEditText(getString(R.string.default_score_zero), i);
            }
            scoreField.addTextChangedListener(scoreTextWatcher);
            playerScores.add(scoreField);
        }

        for (int i = numActiveFields; i < MAX_PLAYERS; i++) {
            EditText scoreField = setupEditText(getString(R.string.default_score_zero), i);
            scoreField.addTextChangedListener(scoreTextWatcher);
            playerScores.add(scoreField);
        }

        table = findViewById(R.id.LayoutForEdittexts);
        lp = setupLinearLayoutParameters();
    }

    private void setupDifficultyToggle() {
        difficultyToggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        difficultyToggle.setup();
    }

    private final TextWatcher playerNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String strPlayerCount = edtPlayerCount.getText().toString();
            if (!strPlayerCount.isEmpty()) {
                numActiveFields = Integer.parseInt(strPlayerCount);
                if (numActiveFields > MAX_PLAYERS) {
                    Toast.makeText(getApplicationContext(), R.string.input_less_message, Toast.LENGTH_SHORT).show();
                    edtPlayerCount.setText("");
                } else {
                    updateScoreFields();
                }
            } else {
                numActiveFields = 0;
                table.removeAllViews();
            }
            updateTotalScore();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void updateScoreFields() {
        table.removeAllViews();
        for (int i = 0; i < numActiveFields; i++) {
            table.addView(playerScores.get(i), lp);
        }
    }

    private void updateTotalScore() {
        totalScore = 0;
        int numFilledFields = 0;
        for (int i = 0; i < numActiveFields; i++) {
            String score = playerScores.get(i).getText().toString();
            if (!score.isEmpty()) {
                totalScore += Integer.parseInt(score);
                numFilledFields++;
            }
        }
        isFilled = (numActiveFields > 0 && numFilledFields == numActiveFields);

        String text = getString(R.string.score_colon) + totalScore;
        tvTotalScore.setText(text);
    }

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateTotalScore();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isFilled) {
            String achievementEarned = getAchievementName(totalScore);

            saveGame(achievementEarned);
            celebrate(achievementEarned);

            new CountDownTimer(COMPLETE, TICK) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    finish();
                }
            }.start();
        } else {
            Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private String getAchievementName(int score) {
        int index = AchievementCalculator.getScorePlacement(
                themeTitles.length, numActiveFields, gameConfig.getPoorScore(),
                gameConfig.getGoodScore(), score, difficultyToggle.getScaleFactor());
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            return titleSubLevelOne;
        } else {
            return themeTitles[index];
        }
    }

    private void saveGame(String achievementEarned) {
        String[] scores = new String[numActiveFields];
        for (int i = 0; i < numActiveFields; i++) {
            scores[i] = playerScores.get(i).getText().toString();
        }

        if(isEdit) {
            currentGame.setAchievementEarned(achievementEarned);
            currentGame.setNumOfPlayers(numActiveFields);
            currentGame.setGroupScore(totalScore);
            currentGame.setScaleFactor(difficultyToggle.getScaleFactor());
            currentGame.setPlayerScores(scores);
        } else {
            Game game = new Game(achievementEarned, numActiveFields, totalScore,
                                 difficultyToggle.getScaleFactor(), scores);
            gameConfig.addGame(game);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    private void celebrate(String achievementEarned) {
        FragmentManager manager = getSupportFragmentManager();
        CelebrationFragment dialog = new CelebrationFragment(achievementEarned);
        dialog.show(manager, CELEBRATION_FRAGMENT);
    }

    private LinearLayout.LayoutParams setupLinearLayoutParameters() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); // Verbose!
        lp.weight = 1.0f; // This is critical. Doesn't work without it.
        edtPlayerCount.measure(0, 0);
        int widthPlayerCountBox = edtPlayerCount.getMeasuredWidth();
        int widthParent = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        int hMargin = (widthParent - widthPlayerCountBox) / 2;
        lp.setMargins(hMargin, 10, hMargin, 10);
        return lp;
    }

    private EditText setupEditText(String text, int row) {
        EditText editText = new EditText(getApplicationContext());
        editText.setText(text);
        editText.setHint(getString(R.string.score_num) + (row + 1));
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
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
