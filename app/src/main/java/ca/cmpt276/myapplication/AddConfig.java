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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.GameConfig;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class AddConfig extends AppCompatActivity {
    public static final String DEFAULT_PHOTO_JPG = "photo.jpg";

    //UI variables
    private EditText edtPoorScore;
    private EditText edtGoodScore;
    private EditText edtConfigName;
    private ImageView ivConfigPhoto;

    // Config variables
    private Boolean isEdit;
    private ConfigManager configManager;
    private GameConfig gameConfig;

    // Feature
    private Camera camera;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(camera.photoFile.getAbsolutePath());
                ivConfigPhoto.setImageBitmap(takenImage);
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
        setContentView(R.layout.activity_add_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.addConfigTitle));
        setupUI();

        configManager = ConfigManager.getInstance();
        camera = new Camera(AddConfig.this,activityLauncher);
        int position=-1;

        if(getIntent().getExtras()!=null)
        {
            isEdit=true;
            position=getIntent().getIntExtra(AddGame.CONFIG_POSITION,-1);
            gameConfig=configManager.getGameConfigAtIndex(position);
            setTitle(getString(R.string.editConfigTitle) + gameConfig.getConfigTitle());
            loadData();
        }
        else {
            isEdit = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_x_menu, menu);
        return true;
    }

    private void setupUI() {
        edtPoorScore = findViewById(R.id.poorScore);
        edtGoodScore = findViewById(R.id.goodScore);
        edtConfigName = findViewById(R.id.configName);
        ivConfigPhoto = findViewById(R.id.configImage);
        ivConfigPhoto.setOnClickListener(view-> camera.askCameraPermission());
    }


    private void loadData() {
        edtPoorScore.setText(Integer.toString(gameConfig.getPoorScore()));
        edtGoodScore.setText(Integer.toString(gameConfig.getGoodScore()));
        edtConfigName.setText(gameConfig.getConfigTitle());

        if (gameConfig.getPhotoFileName() != null) {
            Bitmap takenImage = BitmapFactory.decodeFile(camera.getPhotoFileUri(gameConfig.getPhotoFileName()).getAbsolutePath());
            if (takenImage != null) {
                ivConfigPhoto.setImageBitmap(takenImage);
            }
        }
        TextView tvPhotoCaption = findViewById(R.id.tvPhotoHelp);
        tvPhotoCaption.setText(getString(R.string.config_photo_caption) + gameConfig.getConfigTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        } else if (id == R.id.save) {
            if(isReadyForSave()) {
                if(camera.photoFileName.equals(DEFAULT_PHOTO_JPG) && !isEdit) {
                    showConfirmDialogBox();
                } else {
                    closeActivity();
                }
            } else {
                Toast.makeText(AddConfig.this, R.string.addEmptyMsg, Toast.LENGTH_LONG)
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(this, getString(R.string.camera_permission_msg), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void closeActivity() {
        saveConfig();
        onBackPressed();
    }

    private boolean isReadyForSave() {
        String goodScore = edtGoodScore.getText().toString();
        String poorScore = edtPoorScore.getText().toString();
        String configName = edtConfigName.getText().toString();
        return !goodScore.isEmpty() && !poorScore.isEmpty() && !configName.isEmpty();
    }

    private void saveConfig() {
        if(isEdit)
        {
            gameConfig.setGoodScore(Integer.parseInt(edtGoodScore.getText().toString()));
            gameConfig.setPoorScore(Integer.parseInt(edtPoorScore.getText().toString()));
            gameConfig.setConfigTitle(edtConfigName.getText().toString());
            if(!camera.photoFileName.equals(DEFAULT_PHOTO_JPG))
            {
                gameConfig.setPhotoFileName(camera.photoFileName);
            }
        }
        else
        {
            GameConfig gameConfig = new GameConfig(
                    edtConfigName.getText().toString(),
                    Integer.parseInt(edtPoorScore.getText().toString()),
                    Integer.parseInt(edtGoodScore.getText().toString()),
                    camera.photoFileName);
            configManager.addGame(gameConfig);
        }
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public static Intent makeIntent(Context context,boolean isEdit,int position) {
        Intent intent = new Intent(context, AddConfig.class);
        if(isEdit)
            intent.putExtra(AddGame.CONFIG_POSITION, position);
        return intent;
    }
}
