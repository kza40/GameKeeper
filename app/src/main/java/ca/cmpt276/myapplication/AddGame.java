package ca.cmpt276.myapplication;
import static ca.cmpt276.myapplication.Camera.CAMERA_PERMISSION_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ca.cmpt276.myapplication.model.AchievementCalculator;
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
    private ImageView imageViewPicture;
    private DifficultyToggle difficultyToggle;
    private ScoreCalculator scoreCalculator;
    private AchievementManager achievementManager;

    //Game Variables
    private ConfigManager configManager;
    private GameConfig gameConfig;
    private Game currentGame;
    private boolean isEdit;



    Camera camera;


    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(camera.photoFile.getAbsolutePath());
                imageViewPicture.setImageBitmap(takenImage);
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
        camera=new Camera(AddGame.this,this,activityLauncher);
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
        imageViewPicture = findViewById(R.id.imageViewSelfie);

        View view = findViewById(android.R.id.content).getRootView();
        difficultyToggle = new DifficultyToggle(view);
        difficultyToggle.setup();

        if (isEdit) {
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
            if (currentGame.getPhotoFileName() != null) {
                Bitmap takenImage = BitmapFactory.decodeFile(camera.getPhotoFileUri(currentGame.getPhotoFileName()).getAbsolutePath());
                if (takenImage != null) {
                    imageViewPicture.setImageBitmap(takenImage);
                }
            }
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
                }
                else
                {
                    showConfirmDialogBox();
                }
            }
            else
            {
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
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String achievementEarned = getAchievementName(scoreCalculator.getTotalScore());
                        saveGame(achievementEarned);
                        celebrate(achievementEarned);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
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

    private void saveGame(int achievementPos) {
        if(isEdit) {
            currentGame.setAchievementPos(achievementPos);
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
