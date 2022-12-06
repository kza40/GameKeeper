package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ca.cmpt276.myapplication.model.DecimalValueFormatter;
import ca.cmpt276.myapplication.ui_features.AchievementManager;

public class AchievementStatistics extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<BarEntry> achievementData;
    private int[]achievementPosCounter;

    private static final String ACHIEVEMENT_POS_COUNTER = "ca.cmpt276.myapplication: achievementPosCounter";
    private static final String THEME = "ca.cmpt276.myapplication: theme";
    private static final int[] CHART_COLOURS = { Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80), Color.rgb(171, 222, 230), Color.rgb(203,170,203),
            Color.rgb(173, 247, 182), Color.rgb(171, 196,255) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.achievementStats);

        //Initialize achievementPosCounter from gameConfig class
        Bundle extras = getIntent().getExtras();
        achievementPosCounter = extras.getIntArray(ACHIEVEMENT_POS_COUNTER);

        inputDataToChart();
        setupBarChart();
        formatChartAxis();
        setupLegend();
    }

    private void setupLegend() {
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(16f);
        legend.setTextSize(12f);
    }

    private void setupBarChart() {
        barChart = findViewById(R.id.barChart);
        BarDataSet barDataSet = new BarDataSet(achievementData, getString(R.string.achievementLevels));
        barDataSet.setColors(CHART_COLOURS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        DecimalValueFormatter formatter = new DecimalValueFormatter();
        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(formatter);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.setDescription(description);
        barChart.animateY(2000);
    }

    private void inputDataToChart() {
        achievementData = new ArrayList<>();
        for (int i = 0; i < AchievementManager.NUMBER_OF_ACHIEVEMENT_POS; i++) {
            achievementData.add(new BarEntry(i, achievementPosCounter[i]));
        }
    }

    private void formatChartAxis() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawLabels(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawLabels(false);

        //Setting xAxis Labels
        View view = findViewById(android.R.id.content).getRootView();
        AchievementManager achievementManager = new AchievementManager(view, getIntent().getStringExtra(THEME));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(achievementManager.getTitles()));
        xAxis.setLabelCount(achievementManager.getNumAchievements());
        xAxis.setLabelRotationAngle(-295);
    }

    public static Intent makeIntent(Context context, int[] achievementPosCounter, String theme) {
        Intent intent = new Intent(context, AchievementStatistics.class);
        intent.putExtra(ACHIEVEMENT_POS_COUNTER, achievementPosCounter);
        intent.putExtra(THEME, theme);
        return intent;
    }
}
