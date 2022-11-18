package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ThemeSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);


    }


    public void starWarsSelected(View view){
        displayToast(getString(R.string.star_wars_theme));
    }
    public void fitnessSelected(View view){
        displayToast(getString(R.string.fitness_theme));
    }
    public void spongeBobSelected(View view){
        displayToast(getString(R.string.sponge_bob_theme));
    }

    public void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}