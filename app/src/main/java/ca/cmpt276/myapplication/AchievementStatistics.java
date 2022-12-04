package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ca.cmpt276.myapplication.model.DecimalValueFormatter;
import ca.cmpt276.myapplication.ui_features.AchievementManager;

public class AchievementStatistics extends AppCompatActivity {
    private static final String ACHIEVEMENT_POS_COUNTER = "ca.cmpt276.myapplication: achievementPosCounter";
    private int[] achievementPosCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_statistics);
        Bundle extras = getIntent().getExtras();
        achievementPosCounter = extras.getIntArray(ACHIEVEMENT_POS_COUNTER);

        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> achievementData = new ArrayList<>();
        for (int i = 0; i < AchievementManager.NUMBER_OF_ACHIEVEMENT_POS; i++) {
            achievementData.add(new BarEntry((i + 1), achievementPosCounter[i]));
        }

        DecimalValueFormatter formatter = new DecimalValueFormatter();
        BarDataSet barDataSet = new BarDataSet(achievementData, "Levels");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        //barDataSet.setValueFormatter(formatter);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(formatter);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Achievement Statistics");
        barChart.animateY(2000);


    }

    public static Intent makeIntent(Context context, int[] achievementPosCounter) {
        Intent intent = new Intent(context, AchievementStatistics.class);
        intent.putExtra(ACHIEVEMENT_POS_COUNTER, achievementPosCounter);
        return intent;
    }
}