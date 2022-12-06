package ca.cmpt276.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;


public class Camera {
    private final Activity activity;
    private final ActivityResultLauncher<Intent> activityResultLauncher;

    public Camera(Activity activity, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.activity = activity;
        this.activityResultLauncher=activityResultLauncher;
    }

    public static final int CAMERA_PERMISSION_CODE = 101;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    public File photoFile;

    public void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else
        {
            openCamera();
        }
    }

    public void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFileName=System.currentTimeMillis()+"_"+photoFileName;
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(activity, "com.codepath.fileprovider", photoFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (camera.resolveActivity(activity.getPackageManager()) != null) {
            activityResultLauncher.launch(camera);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }
}
