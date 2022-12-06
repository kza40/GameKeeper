package ca.cmpt276.myapplication;
import static ca.cmpt276.myapplication.Camera.CAMERA_PERMISSION_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

    public static final String DEFAULT_PHOTO_JPG = "photo.jpg";

    //Features
    private DifficultyToggle difficultyToggle;
    private ScoreCalculator scoreCalculator;
    private AchievementManager achievementManager;

    private ImageView imageViewPicture;
    private Camera camera;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private boolean isEdit;
    private int gamePos;
    private int configPos;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(camera.photoFile.getAbsolutePath());
                imageViewPicture.setImageBitmap(takenImage);
            }
            else
            {
                camera.photoFileName= camera.photoFileName.substring(camera.photoFileName.indexOf("_")+1);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.add_game_title));
        camera=new Camera(AddGame.this,activityLauncher);
        setupGameObjects();
        setupFeatures();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_x_menu, menu);
        return true;
    }

    private void setupGameObjects() {
        Intent intent = getIntent();
        configPos = intent.getIntExtra(CONFIG_POSITION, -1);
        configManager = ConfigManager.getInstance();
        gameConfig = configManager.getGameConfigAtIndex(configPos);
        isEdit = false;

        if (intent.getIntExtra(GAME_POSITION, -1) != -1) {
            setTitle(getString(R.string.edit_game_title));
            isEdit = true;
            gamePos = intent.getIntExtra(GAME_POSITION, -1);
            currentGame = gameConfig.getGameAtIndex(gamePos);
        }
    }

    private void setupFeatures() {
        imageViewPicture = findViewById(R.id.imageViewSelfie);

        View view = findViewById(android.R.id.content).getRootView();
        difficultyToggle = new DifficultyToggle(view);
        difficultyToggle.setup();

        if (isEdit) {
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
            setPhoto();
        }

        achievementManager = new AchievementManager(view, configManager.getTheme());
        scoreCalculator = new ScoreCalculator(view, getApplicationContext(), currentGame,isEdit);

        imageViewPicture.setOnClickListener(view1-> {
            if(scoreCalculator.isReadyForSave())
            {
                camera.askCameraPermission();
            }
            else
            {
                Toast.makeText(AddGame.this, R.string.addEmptyMsgforCamera, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setPhoto() {
        if (currentGame.getPhotoFileName() != null) {
            Bitmap takenImage = BitmapFactory.decodeFile(camera.getPhotoFileUri(currentGame.getPhotoFileName()).getAbsolutePath());
            if (takenImage != null) {
                imageViewPicture.setImageBitmap(takenImage);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (scoreCalculator.isReadyForSave()) {
            if(camera.photoFileName.equals(DEFAULT_PHOTO_JPG))
            {
                if(isEdit)
                {
                    if(currentGame.getPhotoFileName().equals(DEFAULT_PHOTO_JPG))
                    {
                        showConfirmDialogBox();
                    }
                    else
                    {
                        closeActivity();
                    }
                }
                else
                {
                    showConfirmDialogBox();
                }
            }
            else
            {
                closeActivity();
            }
        }
        else
        {
            Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private void showConfirmDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmDialogPicture)
                .setPositiveButton(R.string.yes, (dialog, id) -> closeActivity())
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera.openCamera();
            } else {
                Toast.makeText(this, "Camera permission required to use Camera.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void closeActivity() {
        int achievementPos = achievementManager.getAchievementPos(
                scoreCalculator.getTotalScore(),
                scoreCalculator.getNumPlayers(),
                gameConfig.getPoorScore(),
                gameConfig.getGoodScore(),
                difficultyToggle.getScaleFactor());
        saveGame(achievementPos);
        celebrate(achievementPos);
        finish();
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
            if(!camera.photoFileName.equals(DEFAULT_PHOTO_JPG))
            {
                currentGame.setPhotoFileName(camera.photoFileName);
            }
        }
        else {
            Game game = new Game(
                    achievementPos,
                    scoreCalculator.getNumPlayers(),
                    scoreCalculator.getTotalScore(),
                    difficultyToggle.getScaleFactor(),
                    scoreCalculator.getScoresAsArray(),
                    camera.photoFileName);
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
        Intent intent;
        if(isEdit)
        {
            intent = CelebrationPage.makeIntent(this, achievementPos, pointDifference,gamePos,configPos);

        }
        else
        {
            intent = CelebrationPage.makeIntent(this, achievementPos, pointDifference,gameConfig.getGames().size()-1,configPos);
        }
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
