package ca.cmpt276.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ca.cmpt276.myapplication.model.AchievementCalculator;
import ca.cmpt276.myapplication.model.Game;
import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddGame extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_CODE = 101;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File photoFile;
    //Constants
    public static final String CONFIG_POSITION = "AddGame: Config position";
    public static final String Game_POSITION = "AddGame: Game position";
    private static final int TICK = 1000;
    private static final int COMPLETE = 10000;
    public static final int MAX_PLAYERS = 25;
    public static final String CELEBRATION_FRAGMENT = "CelebrationFragment";

    //UI Variables
    private TextView tvTotalScore;
    private TextView tvDifficulty;
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
    private String[] individualScores;
    private String[] themeTitles;
    private String titleSubLevelOne;
    private String achievementEarned;


    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("Testing","ONResult");
            if (result.getResultCode() == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView selfie=findViewById(R.id.imageViewSelfie);
                selfie.setImageBitmap(takenImage);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        ImageView selfie=findViewById(R.id.imageViewSelfie);
        selfie.setImageResource(R.drawable.starwars1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_game));
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

        if( intent.getIntExtra(Game_POSITION, -1)!=-1)
        {
            isEdit=true;
            int gamePos=intent.getIntExtra(Game_POSITION, -1);
            currentGame=gameConfig.getGameAtIndex(gamePos);
        }
    }

    private void setupUIElements() {
        tvTotalScore = findViewById(R.id.tvTotalScore);
        edtNumPlayers = findViewById(R.id.edtNumPlayersDisplay);
        setupDifficultyToggle(isEdit);
        populateEdittextScores(isEdit);
        if(isEdit)
        {
            tvTotalScore.setText("Score: "+currentGame.getGroupScore());
            difficultyToggle.setDifficulty(currentGame.getScaleFactor());
            edtNumPlayers.setText(Integer.toString(currentGame.getNumOfPlayers()));
            updateAchievement(currentGame.getGroupScore(), currentGame.getNumOfPlayers());
        }
        else
        {
            tvTotalScore.setText(R.string.score_equals_zero);
        }
        edtNumPlayers.addTextChangedListener(playerNumTextWatcher);
    }

    private void setupDifficultyToggle(boolean isEdit) {
        difficultyToggle = new DifficultyToggle(findViewById(android.R.id.content).getRootView());
        difficultyToggle.setup();
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDifficulty.addTextChangedListener(scoreTextWatcher);
    }

    private void populateEdittextScores(Boolean isEdit) {
        if(isEdit)
        {
            NUM_ROWS = currentGame.getNumOfPlayers();
            edtIndividualScore = new EditText[NUM_ROWS];
        }
        LinearLayout table = (LinearLayout) findViewById(R.id.LayoutForEdittexts);
        table.removeAllViews();

        for (int row = 0; row < NUM_ROWS; row++) {
            EditText editText = setupEditText(row);
            LinearLayout.LayoutParams lp = setupLinearLayoutParameters();

            if(isEdit)
            {
                editText.setText(currentGame.getPlayerScoresAtIndex(row));
            }
            table.addView(editText, lp);
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
            totalScore=0;
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
                tvTotalScore.setText(getString(R.string.score_colon_2) + totalScore);
                if (individualScoresChecker) {
                    updateAchievement(totalScore, Integer.parseInt(numPlayersInput));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void updateAchievement(int score, int numPlayers) {
        int index = AchievementCalculator.getScorePlacement(
                themeTitles.length, numPlayers, gameConfig.getPoorScore(), gameConfig.getGoodScore(),
                score, difficultyToggle.getScaleFactor());
        if (index == AchievementCalculator.INDEX_SUB_LEVEL_ONE) {
            achievementEarned = titleSubLevelOne;
        } else {
            achievementEarned = themeTitles[index];
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String numPlayers = edtNumPlayers.getText().toString();

        if (!numPlayers.isEmpty() && totalScore != 0) {
            askCameraPermission();
//            saveGame(Integer.parseInt(numPlayers), totalScore);
//            celebrate();
//            new CountDownTimer(COMPLETE, TICK) {
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    finish();
//                }
//            }.start();
        } else {
            Toast.makeText(AddGame.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private void askCameraPermission() {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
            else
            {
                openCamera();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission required to use Camera.", Toast.LENGTH_LONG).show();
//                celebrate();
//                new CountDownTimer(Complete, Tick) {
//                    public void onTick(long millisUntilFinished) {
//                    }
//
//                    public void onFinish() {
//                        finish();
//                    }
//                }.start();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(AddGame.this, "com.codepath.fileprovider", photoFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (camera.resolveActivity(getPackageManager()) != null) {
            activityLauncher.launch(camera);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void celebrate() {
        FragmentManager manager = getSupportFragmentManager();
        CelebrationFragment dialog = new CelebrationFragment(achievementEarned);
        dialog.show(manager, CELEBRATION_FRAGMENT);
    }

    private void saveGame(int numPlayers, int groupScore) {
        if(isEdit)
        {
            currentGame.setAchievementEarned(achievementEarned);
            currentGame.setNumOfPlayers(numPlayers);
            currentGame.setGroupScore(groupScore);
            currentGame.setScaleFactor(difficultyToggle.getScaleFactor());
            currentGame.setPlayerScores(individualScores);
        }
        else
        {
            Game game = new Game(achievementEarned, numPlayers, groupScore, gameConfig.getPoorScore(),
                    gameConfig.getGoodScore(), difficultyToggle.getScaleFactor(), individualScores);
            gameConfig.addGame(game);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
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
        if(isEdit)
            intent.putExtra(Game_POSITION, gamePosition);
        return intent;
    }
}

