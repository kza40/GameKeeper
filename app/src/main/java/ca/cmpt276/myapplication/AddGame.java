package ca.cmpt276.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;
import ca.cmpt276.myapplication.ui_features.AchievementManager;
import ca.cmpt276.myapplication.ui_features.DifficultyToggle;
import ca.cmpt276.myapplication.ui_features.ScoreCalculator;

public class AddGame extends AppCompatActivity {
    //Constants
    public static final String CONFIG_POSITION = "AddGame: Config position";
    public static final String GAME_POSITION = "AddGame: Game position";

    //Features
    private DifficultyToggle difficultyToggle;
    private ScoreCalculator scoreCalculator;
    private AchievementManager achievementManager;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private boolean isEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.add_game_title));

        setupGameObjects();
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

    private void setupFeatures() {
        View view = findViewById(android.R.id.content).getRootView();
        difficultyToggle = new DifficultyToggle(view);
        difficultyToggle.setup();
        if (isEdit) {
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
        }
        scoreCalculator = new ScoreCalculator(view, getApplicationContext(), currentGame);
        achievementManager = new AchievementManager(view, configManager.getTheme());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (scoreCalculator.isReadyForSave()) {
            int achievementPos = achievementManager.getAchievementPos(
                    scoreCalculator.getTotalScore(),
                    scoreCalculator.getNumPlayers(),
                    gameConfig.getPoorScore(),
                    gameConfig.getGoodScore(),
                    difficultyToggle.getScaleFactor());

            saveGame(achievementPos);
            celebrate(achievementPos);
            finish();

        } else {
            Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private void saveGame(int achievementPos) {
        if(isEdit) {
            gameConfig.getAchievementPosCounter()[currentGame.getAchievementPos()]--;
            currentGame.setAchievementPos(achievementPos);
            gameConfig.getAchievementPosCounter()[currentGame.getAchievementPos()]++;
            currentGame.setScaleFactor(difficultyToggle.getScaleFactor());
            currentGame.setNumOfPlayers(scoreCalculator.getNumPlayers());
            currentGame.setGroupScore(scoreCalculator.getTotalScore());
            currentGame.setPlayerScores(scoreCalculator.getScoresAsArray());
        }
        else {
            Game game = new Game(
                    achievementPos,
                    scoreCalculator.getNumPlayers(),
                    scoreCalculator.getTotalScore(),
                    difficultyToggle.getScaleFactor(),
                    scoreCalculator.getScoresAsArray());
            gameConfig.addGame(game);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    private void celebrate(int achievementPos) {
        int pointDifference = achievementManager.getPointsToNextLevel(
                        scoreCalculator.getTotalScore(),
                        scoreCalculator.getNumPlayers(),
                        gameConfig.getPoorScore(),
                        gameConfig.getGoodScore(),
                        difficultyToggle.getScaleFactor(),
                        achievementPos);

        Intent intent = CelebrationPage.makeIntent(this, achievementPos, pointDifference);
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
