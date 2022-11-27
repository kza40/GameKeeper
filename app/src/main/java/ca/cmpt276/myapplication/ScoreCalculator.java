package ca.cmpt276.myapplication;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.myapplication.model.Game;

public class ScoreCalculator {
    public static final int MAX_PLAYERS = 25;

    private final Context mContext;
    private final View mView;
    private final Game mCurrentGame;

    private TextView tvTotalScore;
    private EditText edtPlayerCount;
    private LinearLayout table;
    private LinearLayout.LayoutParams lp;

    private List<EditText> playerScores;
    private boolean isFilled;
    private int numActiveFields;
    private int totalScore;

    public ScoreCalculator(View view, Context context, Game currentGame) {
        mView = view;
        mContext = context;
        mCurrentGame = currentGame;
        setupUI();
    }

    private void setupUI() {
        tvTotalScore = mView.findViewById(R.id.tvTotalScore);
        edtPlayerCount = mView.findViewById(R.id.edtNumPlayersDisplay);

        if (mCurrentGame != null) {
            totalScore = mCurrentGame.getGroupScore();
            String count = Integer.toString(mCurrentGame.getNumOfPlayers());
            edtPlayerCount.setText(count);
        } else {
            totalScore = 0;
            edtPlayerCount.setText(mContext.getString(R.string.default_player_count));
        }

        String text = mContext.getString(R.string.score_colon) + totalScore;
        tvTotalScore.setText(text);

        numActiveFields = Integer.parseInt(edtPlayerCount.getText().toString());

        loadPlayerScores();
        updateScoreFields();

        edtPlayerCount.addTextChangedListener(playerNumTextWatcher);
        isFilled = true;
    }

    private void loadPlayerScores() {
        playerScores = new ArrayList<>();

        for (int i = 0; i < numActiveFields; i++) {
            EditText scoreField;
            if (mCurrentGame != null) {
                scoreField = setupEditText("" + mCurrentGame.getPlayerScoresAtIndex(i), i);
            }
            else {
                scoreField = setupEditText(mContext.getString(R.string.default_score_zero), i);
            }
            scoreField.addTextChangedListener(scoreTextWatcher);
            playerScores.add(scoreField);
        }

        for (int i = numActiveFields; i < MAX_PLAYERS; i++) {
            EditText scoreField = setupEditText(mContext.getString(R.string.default_score_zero), i);
            scoreField.addTextChangedListener(scoreTextWatcher);
            playerScores.add(scoreField);
        }

        table = mView.findViewById(R.id.LayoutForEdittexts);
        lp = setupLinearLayoutParameters();
    }

    private void updateScoreFields() {
        table.removeAllViews();
        for (int i = 0; i < numActiveFields; i++) {
            table.addView(playerScores.get(i), lp);
        }
    }

    private void updateTotalScore() {
        totalScore = 0;
        int numScoresFilled = 0;
        for (int i = 0; i < numActiveFields; i++) {
            String score = playerScores.get(i).getText().toString();
            if (!score.isEmpty()) {
                totalScore += Integer.parseInt(score);
                numScoresFilled++;
            }
        }
        isFilled = (numActiveFields > 0 && numScoresFilled == numActiveFields);

        String text = mContext.getString(R.string.score_colon) + totalScore;
        tvTotalScore.setText(text);
    }

    private final TextWatcher playerNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String strPlayerCount = edtPlayerCount.getText().toString();
            if (!strPlayerCount.isEmpty()) {
                numActiveFields = Integer.parseInt(strPlayerCount);
                if (numActiveFields > MAX_PLAYERS) {
                    Toast.makeText(mContext, mContext.getString(R.string.input_less_message),
                            Toast.LENGTH_SHORT).show();
                    edtPlayerCount.setText("");
                } else {
                    updateScoreFields();
                }
            } else {
                numActiveFields = 0;
                table.removeAllViews();
            }

            updateTotalScore();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private final TextWatcher scoreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateTotalScore();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };


    private EditText setupEditText(String text, int row) {
        EditText editText = new EditText(mContext);
        editText.setText(text);
        editText.setHint(mContext.getString(R.string.score_num) + (row + 1));
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        return editText;
    }

    private LinearLayout.LayoutParams setupLinearLayoutParameters() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); // Verbose!
        lp.weight = 1.0f; // This is critical. Doesn't work without it.
        edtPlayerCount.measure(0, 0);
        int widthPlayerCountBox = edtPlayerCount.getMeasuredWidth();
        int widthParent = mContext.getResources().getDisplayMetrics().widthPixels;
        int hMargin = (widthParent - widthPlayerCountBox) / 2;
        lp.setMargins(hMargin, 10, hMargin, 10);
        return lp;
    }

    public int getNumPlayers() {
        return numActiveFields;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public boolean isReadyForSave() {
        return isFilled;
    }

    public String[] getScoresAsArray() {
        String[] scores = new String[numActiveFields];
        for (int i = 0; i < numActiveFields; i++) {
            scores[i] = playerScores.get(i).getText().toString();
        }
        return scores;
    }
}
