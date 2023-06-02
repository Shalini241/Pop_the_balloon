package com.pop_up_balloon.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.pop_up_balloon.activities.CreateNewPlayerActivity;
import com.pop_up_balloon.R;

/**
 * Class to create dialog box when it is score high score
 */
public class HighScoreDialogFragment extends DialogFragment {

    private final int mScore;

    public HighScoreDialogFragment(int score){
        this.mScore = score;
    }

    /**
     * This method creates dialog box to save the high score
     *
     * @param savedInstanceState
     * @return Dialog box
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.new_high_score_title)+mScore)
                .setPositiveButton("Save", (DialogInterface.OnClickListener) (dialog, id) -> {
                    Intent intent = new Intent(getActivity(), CreateNewPlayerActivity.class);
                    intent.putExtra("Score", mScore);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, id) -> {
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
