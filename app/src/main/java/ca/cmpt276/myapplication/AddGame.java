package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private static final int TICK = 1000;
    private static final int COMPLETE = 10000;
    public static final String CELEBRATION_FRAGMENT = "CelebrationFragment";

    //Features
    private DifficultyToggle difficultyToggle;
    private ScoreCalculator scoreCalculator;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private boolean isEdit;

    private int nextBoundary;

    //Achievements
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
        setupFeatures();
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

    private void setupFeatures() {
        View view = findViewById(android.R.id.content).getRootView();
        difficultyToggle = new DifficultyToggle(view);
        difficultyToggle.setup();
        if (isEdit) {
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
        }

        scoreCalculator = new ScoreCalculator(view, getApplicationContext(), currentGame);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (scoreCalculator.isReadyForSave()) {
            String achievementEarned = getAchievementName(scoreCalculator.getTotalScore());
            nextBoundary = AchievementCalculator.getNextBoundary(scoreCalculator.getTotalScore()) - scoreCalculator.getTotalScore();
            saveGame(achievementEarned);
            celebrate(achievementEarned, nextBoundary);

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
                themeTitles.length, scoreCalculator.getNumPlayers(), gameConfig.getPoorScore(),
                gameConfig.getGoodScore(), score, difficultyToggle.getScaleFactor()
        );
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            return titleSubLevelOne;
        } else {
            return themeTitles[index];
        }
    }

    private void saveGame(String achievementEarned) {
        if(isEdit) {
            currentGame.setAchievementEarned(achievementEarned);
            currentGame.setScaleFactor(difficultyToggle.getScaleFactor());
            currentGame.setNumOfPlayers(scoreCalculator.getNumPlayers());
            currentGame.setGroupScore(scoreCalculator.getTotalScore());
            currentGame.setPlayerScores(scoreCalculator.getScoresAsArray());
        } else {
            Game game = new Game(achievementEarned, scoreCalculator.getNumPlayers(),
                                 scoreCalculator.getTotalScore(), difficultyToggle.getScaleFactor(),
                                 scoreCalculator.getScoresAsArray());
            gameConfig.addGame(game);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    private void celebrate(String achievementEarned, int nextBoundary) {
        Intent intent = CelebrationPage.makeIntent(this, achievementEarned, nextBoundary);
        startActivity(intent);
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
