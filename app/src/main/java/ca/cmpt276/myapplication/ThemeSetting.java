package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.SharedPreferenceManager;

public class ThemeSetting extends AppCompatActivity {
    public static final String THEME_STAR_WARS="THEME_STAR_WARS";
    public static final String THEME_FITNESS="THEME_FITNESS";
    public static final String THEME_SPONGEBOB="THEME_SPONGEBOB";

    ConfigManager configManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_settings));

        configManager = ConfigManager.getInstance();
    }

    public void starWarsSelected(View view){
        onImageSelected(getString(R.string.star_wars_theme), THEME_STAR_WARS);
    }
    public void fitnessSelected(View view){
        onImageSelected(getString(R.string.fitness_theme), THEME_FITNESS);
    }
    public void spongeBobSelected(View view){
        onImageSelected(getString(R.string.sponge_bob_theme), THEME_SPONGEBOB);
    }

    public void onImageSelected(String themeDisplay, String theme) {
        displayToast("You've selected " + themeDisplay);
        configManager.setTheme(theme);
        new SharedPreferenceManager(getApplicationContext()).updateConfigManager(configManager);
    }

    public void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ThemeSetting.class);
        return intent;
    }
}