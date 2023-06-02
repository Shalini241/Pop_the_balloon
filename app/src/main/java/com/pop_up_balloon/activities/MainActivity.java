package com.pop_up_balloon.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pop_up_balloon.R;
import com.pop_up_balloon.utils.HighScoreHelper;

/**
 * <h1>MainActivity</h1>
 * <p><b>MainActivity</b> represent start screen for the game.</p>
 */
public class MainActivity extends AppCompatActivity {
    public static final String SOUND = "SOUND", MUSIC = "MUSIC";
    private boolean mMusic = true, mSound = true;
    private TextView highScore;
    private Animation animation;

    /**
     * This method is responsible for configurations of game start screen.
     *
     * @param savedInstanceState Define potentially saved parameters due to configurations changes.
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setToFullScreen();
        Button btnStart = findViewById(R.id.btn_start);
        Button btnHighScore = findViewById(R.id.btn_high_score);
        final ImageButton btnMusic = findViewById(R.id.btn_music);
        final ImageButton btnSound = findViewById(R.id.btn_sound);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        animation.setDuration(100);
        highScore = findViewById(R.id.high_score);
        highScore.setText(String.valueOf(HighScoreHelper.getTopScore(this)));
       findViewById(R.id.activity_start).setOnClickListener(view -> setToFullScreen());

        btnStart.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);
            intent.putExtra(SOUND, mSound);
            intent.putExtra(MUSIC, mMusic);
            startActivity(intent);
        });

        btnHighScore.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HighScoreActivity.class);
            startActivity(intent);
        });

        btnMusic.setOnClickListener(view -> {
            if (mMusic) {
                mMusic = false;
                btnMusic.setBackgroundResource(R.drawable.music_note_off);
            } else {
                mMusic = true;
                btnMusic.setBackgroundResource(R.drawable.music_note);
            }
        });

        btnSound.setOnClickListener(view -> {
            if (mSound) {
                mSound = false;
                btnSound.setBackgroundResource(R.drawable.volume_off);
            } else {
                mSound = true;
                btnSound.setBackgroundResource(R.drawable.volume_up);
            }
        });

    }

    /**
     * This method is responsible to transfer MainActivity into fullscreen mode.
     */
    private void setToFullScreen() {
        findViewById(R.id.activity_start).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * This method is responsible for calling method setToFullScreen(), to set user high score
     *
     * @see MainActivity#setToFullScreen()
     * @see Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
        highScore.setText(String.valueOf(HighScoreHelper.getTopScore(this)));
    }
}