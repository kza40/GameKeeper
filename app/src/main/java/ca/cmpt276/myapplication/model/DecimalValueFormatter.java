package ca.cmpt276.myapplication.model;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class DecimalValueFormatter extends ValueFormatter {
    private DecimalFormat mFormat;

    public DecimalValueFormatter() {
        mFormat = new DecimalFormat("#");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }
}

