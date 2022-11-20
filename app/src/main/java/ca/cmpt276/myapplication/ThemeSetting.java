package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ca.cmpt276.myapplication.model.ConfigManager;

public class ThemeSetting extends AppCompatActivity {


    public static final String THEME_STAR_WARS="THEME_STAR_WARS";
    public static final String THEME_FITNESS="THEME_FITNESS";
    public static final String THEME_SPONGEBOB="THEME_SPONGEBOB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);


    }

    //TO DO: Update Shared Preferences anytime a theme is switched
    public void starWarsSelected(View view){
        displayToast("You've selected "+getString(R.string.star_wars_theme));
        ConfigManager.getInstance().setTheme(THEME_STAR_WARS);

    }
    public void fitnessSelected(View view){
        displayToast("You've selected "+getString(R.string.fitness_theme));
        ConfigManager.getInstance().setTheme(THEME_FITNESS);
    }
    public void spongeBobSelected(View view){
        displayToast("You've selected "+getString(R.string.sponge_bob_theme));
        ConfigManager.getInstance().setTheme(THEME_SPONGEBOB);
    }

    public void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ThemeSetting.class);
        return intent;
    }
}