package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
    public static final String CONFIG_POSITION = "AddGame: Config position";
    public static final String Game_POSITION = "AddGame: Game position";

    private TextView txtScore;
    private EditText edtNumPlayers;
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private int NUM_ROWS = 0;
    private EditText[] edtIndividualScore;
    private Boolean isEdit;
    private boolean isScoreFieldsFilled;

    private TextView tvDifficulty;
    private int totalScore;
    private String[] individualScores;
    private String[] themeTitles;
    private String titleSubLevelOne;
    private String achievementEarned;

    private DifficultyToggle toggle;

    private static final int Tick = 1000;
    private static final int Complete = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_game));
        setupMemberVariables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_game_menu, menu);
        return true;
    }

    private void setupMemberVariables() {
        isScoreFieldsFilled = false;

        Intent intent = getIntent();
        int configPos = intent.getIntExtra(CONFIG_POSITION, -1);

        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        isEdit=false;
        int gamePos;
        // TextViews
        txtScore = findViewById(R.id.txtTotalScore);
        // EditText fields
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
        // Achievement-related
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


        // Difficulty toggle
        toggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        toggle.setup();
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDifficulty.addTextChangedListener(scoreTextWatcher);

        if( intent.getIntExtra(Game_POSITION, -1)!=-1)
        {
            isEdit=true;
            isScoreFieldsFilled = true;
            gamePos=intent.getIntExtra(Game_POSITION, -1);
            currentGame=gameConfig.getGameAtIndex(gamePos);

            txtScore.setText("Score: "+currentGame.getGroupScore());
            toggle.setDifficulty(currentGame.getScaleFactor());
            edtNumPlayers.setText(Integer.toString(currentGame.getNumOfPlayers()));
            loadPlayerScoresForEditGame();

            updateAchievement(currentGame.getGroupScore(), currentGame.getNumOfPlayers());
        }
        else
        {
            txtScore.setText("Score: 0");
        }
        edtNumPlayers.addTextChangedListener(playerNumTextWatcher);
    }

    private void loadPlayerScoresForEditGame() {
        NUM_ROWS = currentGame.getNumOfPlayers();
        edtIndividualScore = new EditText[NUM_ROWS];
        LinearLayout table = (LinearLayout) findViewById(R.id.LayoutForEdittexts);
        table.removeAllViews();

        for (int row = 0; row < NUM_ROWS; row++) {
            EditText editText = new EditText(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); // Verbose!
            lp.weight = 1.0f; // This is critical. Doesn't work without it.
            lp.setMargins(240, 10, 240, 10);

            editText.setHint("Score #" + (row + 1));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            editText.setText(currentGame.getPlayerScoresAtIndex(row));
            table.addView(editText, lp);

            //EditText fields
            edtIndividualScore[row] = editText;
            edtIndividualScore[row].addTextChangedListener(scoreTextWatcher);
        }
    }


    private final TextWatcher playerNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numPlayersInput = edtNumPlayers.getText().toString();

            if (!numPlayersInput.isEmpty()) {
                NUM_ROWS = Integer.parseInt(numPlayersInput);
                if (NUM_ROWS > 200) {
                    Toast.makeText(getApplicationContext(), "Please input a number less than 200", Toast.LENGTH_SHORT).show();
                    edtNumPlayers.setText("");
                } else {
                    edtIndividualScore = new EditText[NUM_ROWS];
                    populateEdittextScores();
                }
            } else {
                LinearLayout table = (LinearLayout) findViewById(R.id.LayoutForEdittexts);
                table.removeAllViews();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

        private TextWatcher scoreTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String numPlayersInput = edtNumPlayers.getText().toString();
                individualScores = new String[NUM_ROWS];
                boolean individualScoresChecker = true;
                totalScore = 0;
                for (int row = 0; row < NUM_ROWS; row++) {
                    individualScores[row] = edtIndividualScore[row].getText().toString();
                    if (individualScores[row].isEmpty()) {
                        individualScoresChecker = false;
                        totalScore = 0;
                    }
                    if (individualScoresChecker) {
                        totalScore += Integer.parseInt(individualScores[row]);
                    }
                }
                if (!numPlayersInput.isEmpty()) {
                    txtScore.setText("Score: " + totalScore);
                    if (individualScoresChecker) {
                        isScoreFieldsFilled = true;
                        updateAchievement(totalScore, Integer.parseInt(numPlayersInput));
                    }
                    else {
                        isScoreFieldsFilled = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        private void populateEdittextScores() {
            LinearLayout table = (LinearLayout) findViewById(R.id.LayoutForEdittexts);
            table.removeAllViews();

            for (int row = 0; row < NUM_ROWS; row++) {
                EditText editText = new EditText(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); // Verbose!
                lp.weight = 1.0f; // This is critical. Doesn't work without it.
                lp.setMargins(240, 10, 240, 10);

                editText.setHint("Score #" + (row + 1));
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                table.addView(editText, lp);

                //EditText fields
                edtIndividualScore[row] = editText;
                edtIndividualScore[row].addTextChangedListener(scoreTextWatcher);
            }
        }


        private void updateAchievement(int score, int numPlayers) {
            int index = AchievementCalculator.getScorePlacement(
                    themeTitles.length, numPlayers, gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                    score, toggle.getScaleFactor());
            String name;
            if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
                name = titleSubLevelOne;
            } else {
                name = themeTitles[index];
            }
            achievementEarned = name;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            String numPlayers = edtNumPlayers.getText().toString();

            if (!numPlayers.isEmpty() && isScoreFieldsFilled) {
                saveGame(Integer.parseInt(numPlayers), totalScore);
                celebrate();
                new CountDownTimer(Complete, Tick) {
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

        private void celebrate() {
            FragmentManager manager = getSupportFragmentManager();
            CelebrationFragment dialog = new CelebrationFragment(achievementEarned);
            dialog.show(manager, "CelebrationFragment");
        }

        private void saveGame(int numPlayers, int groupScore) {
            if(isEdit)
            {
                currentGame.setAchievementEarned(achievementEarned);
                currentGame.setNumOfPlayers(numPlayers);
                currentGame.setGroupScore(groupScore);
                currentGame.setScaleFactor(toggle.getScaleFactor());
                currentGame.setPlayerScores(individualScores);
            }
            else
            {
                Game game = new Game(achievementEarned, numPlayers, groupScore, gameConfig.getPoorScore(),
                        gameConfig.getGoodScore(), toggle.getScaleFactor(), individualScores);
                gameConfig.addGame(game);
            }
            new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
        }

        public static Intent makeIntent(Context context,boolean isEdit, int position,int gamePosition) {
            Intent intent = new Intent(context, AddGame.class);
            intent.putExtra(CONFIG_POSITION, position);
            if(isEdit)
                intent.putExtra(Game_POSITION, gamePosition);
            return intent;
        }
    }

