package ca.cmpt276.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewAchievements extends AppCompatActivity {

    private static final String EXTRA_POOR_SCORE = "ca.cmpt276.assignment2: poor score";
    private static final String EXTRA_GREAT_SCORE = "ca.cmpt276.assignment2: great score";

    int[] scoresIDs;

    EditText edtNumPlayers;

    private static final int NUM_ACHIEVEMENTS = 8;

    private int poorScore;
    private int greatScore;

    private List<Integer> minScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_achievements);

        Intent intent = getIntent();
        poorScore = intent.getIntExtra(EXTRA_POOR_SCORE, 0);
        greatScore = intent.getIntExtra(EXTRA_GREAT_SCORE, 0);

        minScores = new ArrayList<>();

        scoresIDs = new int[] {R.id.tvScoreLevel1, R.id.tvScoreLevel2, R.id.tvScoreLevel3,
                R.id.tvScoreLevel4, R.id.tvScoreLevel5, R.id.tvScoreLevel6, R.id.tvScoreLevel7,
                R.id.tvScoreLevel8};

        edtNumPlayers = findViewById(R.id.edtNumPlayers);
        edtNumPlayers.addTextChangedListener(scoreTextWatcher);

    }

    private TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numPlayersInput = edtNumPlayers.getText().toString();
            if(!numPlayersInput.isEmpty()) {
                updateScoreFields(Integer.parseInt(numPlayersInput));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void updateScoreFields(int players) {
        updateAchievements(players);
        for (int i = 0; i < NUM_ACHIEVEMENTS; i++) {
            int currID = scoresIDs[i];
            TextView tv = findViewById(currID);
            tv.setText(Integer.toString(minScores.get(i)));
        }
    }

    public static Intent makeIntent(Context context, int poorScore, int greatScore) {
        Intent intent = new Intent(context, PreviewAchievements.class);
        intent.putExtra(EXTRA_POOR_SCORE, poorScore);
        intent.putExtra(EXTRA_GREAT_SCORE, greatScore);
        return intent;
    }

    private void updateAchievements(int numPlayers) {
        minScores.clear();

        int lowerBound = Math.min(numPlayers * poorScore, numPlayers * greatScore);
        minScores.add(lowerBound);

        int upperBound = Math.max(numPlayers * poorScore, numPlayers * greatScore);

        int numIntervals = NUM_ACHIEVEMENTS - 1;
        int range = upperBound - lowerBound;
        // Approximately how much each level should be spaced out (rounded down)
        int jumpAmt = range / numIntervals;

        // In case jumpAmt calculation is not a whole number, the space between Level 7 and Level 8
        // will be larger than the jumpAmt. The rest of the levels will be adjusted

        int extraAmt = range % numIntervals;     // Between 0 to 6
        int indexToBeAdjusted = 0;      // Start adjusting the levels from Level 2
        int timesAdjusted = 0;

        int currScore = lowerBound;
        for (int i = 0; i < NUM_ACHIEVEMENTS - 2; i++) {
            currScore += jumpAmt;
            if (i == indexToBeAdjusted && timesAdjusted < extraAmt) {
                currScore++;
                indexToBeAdjusted += ((NUM_ACHIEVEMENTS - 2) /  extraAmt);
                timesAdjusted++;
            }

            minScores.add(currScore);
        }
        minScores.add(upperBound);

        if (numPlayers * poorScore > numPlayers * greatScore) {
            Collections.reverse(minScores);
        }
    }

}