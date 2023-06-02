package com.pop_up_balloon.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pop_up_balloon.R;
import com.pop_up_balloon.utils.HighScoreDialogFragment;
import com.pop_up_balloon.utils.HighScoreHelper;
import com.pop_up_balloon.utils.SoundHelper;
import com.pop_up_balloon.objects.Balloon;
import com.pop_up_balloon.objects.CircleBalloon;
import com.pop_up_balloon.objects.SquareBalloon;

/**
 * <h1>GamePlayActivity</h1>
 *
 * <p><b>GamePlayActivity</b> represent gameplay screen and handle all game logic.</p>
 */
public class GameplayActivity extends AppCompatActivity implements Balloon.BalloonListener, SensorEventListener {
    private static final int MIN_ANIMATION_DELAY = 500, MAX_ANIMATION_DELAY = 1500;
    private final Random mRandom = new Random();
    private final int[] mBalloonColors = {Color.YELLOW, Color.RED, Color.WHITE, Color.rgb(160, 32, 240), Color.GREEN, Color.rgb(255, 165, 0), Color.BLUE};
    private final int CIRCLE = 0;
    private CountDownTimer countDownTimer;
    private final int SQUARE = 1;
    private final int[] mBalloonShapes = {CIRCLE, SQUARE};
    private int mScreenWidth, mScreenHeight, mSpeed, mScore, mMiss;
    private boolean mSound, mMusic, mGame;
    private TextView mScoreDisplay, tvTime, mMissedDisplay;
    private Button mGoButton;
    private ViewGroup mContentView;
    private SoundHelper mSoundHelper, mMusicHelper;
    private final List<Balloon> mBalloons = new ArrayList<>();
    private Animation mAnimation;
    private SensorManager sensorManager;
    private float tilt;

    private int[] splitScreenStart;

    private AsyncTask<Integer, Integer, Void> balloonLauncher;

    /**
     * This method is responsible for configurations of gameplay screen.
     *
     * @param savedInstanceState Define potentially saved parameters due to configurations changes.
     * @see android.app.Activity#onCreate(Bundle)
     */
    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        getSupportActionBar().hide();
        mContentView = findViewById(R.id.balloon_field);
        ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        mMusicHelper = new SoundHelper(this);
        mMusicHelper.prepareMusicPlayer(this);
        Intent intent = getIntent();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mScreenWidth = mContentView.getWidth();
                    splitScreenStart = new int[]{0, ((mScreenWidth-200)/5)*2,
                            ((mScreenWidth-200)/5)*4};
                    mScreenHeight = mContentView.getHeight();
                }
            });
        }
        mScoreDisplay = findViewById(R.id.score_display);
        mMissedDisplay = findViewById(R.id.missed_display);
        tvTime = findViewById(R.id.timer_display);
        mGoButton = findViewById(R.id.go_button);
        updateDisplay();
        mSoundHelper = new SoundHelper(this);
        mSoundHelper.prepareMusicPlayer(this);
        mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        mAnimation.setDuration(100);

        if (intent.hasExtra(MainActivity.SOUND))
            mSound = intent.getBooleanExtra(MainActivity.SOUND, true);

        if (intent.hasExtra(MainActivity.MUSIC))
            mMusic = intent.getBooleanExtra(MainActivity.MUSIC, true);
    }

    /**
     * This method is responsible to continue play music when user back to the game.
     *
     * @see Activity#onRestart()
     * @see SoundHelper#playMusic()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (mGame) {
            if (mMusic) mMusicHelper.playMusic();
        }
    }

    /**
     * This method is responsible to start game and set beginning game parameters.
     *
     * @see SoundHelper#playMusic()
     */
    private void startGame() {
        mScore = 0;
        mMiss = 0;
        mGame = true;
        if (mMusic) mMusicHelper.playMusic();
        updateDisplay();
        balloonLauncher = new BalloonLauncher().execute(mSpeed);
        tvTime.setText(":59");
        mGoButton.setVisibility(View.INVISIBLE);
        startTimer(60000);
    }

    /**
     * This method is called when button start game is clicked and indicate the beginning of the game if game is in the progress,
     * else it's start new level.
     *
     * @param view represent button which user is tapped.
     * @see GameplayActivity#startGame()
     */
    public void goButtonClickHandler(View view) {
        startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * This method is called when balloon is popped, either by tapping by user or by going away.
     *
     * @param balloon   represent balloon object which is popped.
     * @param userTouch indicate if user popped balloon or balloon is going away.
     */
    @Override
    public void popBalloon(Balloon balloon, boolean userTouch) {
        if (mSound) mSoundHelper.playSound();
        mContentView.removeView(balloon);
        mBalloons.remove(balloon);
        if (userTouch) {
            if(balloon.color == Color.RED && balloon instanceof CircleBalloon) {
                mScore++;
                if (mScore % 10 == 0) {
                    mScore += 10;
                    int newTime = Integer.parseInt(tvTime.getText().toString().substring(1))+10;
                    countDownTimer.cancel();
                    startTimer(newTime*1000);
                }
            } else if(mScore>0){
                mScore--;
            }
        } else if(balloon.color == Color.RED && balloon instanceof CircleBalloon){
            mMiss++;
        }
        updateDisplay();
    }

    /**
     * This method is called when game is over and showing new high score if it is achieved
     *
     * @see HighScoreHelper
     */
    private void gameOver(boolean fromBack) {
        if(!fromBack)
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
        if (mMusic) mMusicHelper.pauseMusic();
        mGame = false;

        for (Balloon balloon : mBalloons) {
            mContentView.removeView(balloon);
            balloon.setPopped(true);
        }
        mBalloons.clear();

        if (HighScoreHelper.isTopScore(this, mScore)) {
            HighScoreHelper.setTopScore(this, mScore);
            HighScoreDialogFragment dialog = new HighScoreDialogFragment(mScore);
            dialog.show(getSupportFragmentManager(), null);

        }
        balloonLauncher.cancel(true);
        countDownTimer.cancel();
        while(!balloonLauncher.isCancelled()){}
    }

    /**
     * This method update score after every popped balloon and level at the beginning of new level.
     */
    private void updateDisplay() {
        mScoreDisplay.setText(String.valueOf(mScore));
        mMissedDisplay.setText(String.valueOf(mMiss));
    }

    /**
     * This method add new balloon to the screen.
     *
     * @param x represent x axis of the balloon.
     * @see Balloon
     * @see ViewGroup#addView(View)
     */
    private void launchBalloon(int x) {
        Balloon balloon;
        if(mBalloonShapes[mRandom.nextInt(mBalloonShapes.length)]==0){
            balloon = new CircleBalloon(this, mBalloonColors[mRandom.nextInt(mBalloonColors.length)],
                    180);
        } else {
            balloon = new SquareBalloon(this, mBalloonColors[mRandom.nextInt(mBalloonColors.length)],
                    180);
        }
        mBalloons.add(balloon);
        balloon.setX(x);
        balloon.setY(mScreenHeight + balloon.getHeight());
        mContentView.addView(balloon);
        Random random = new Random();
        mSpeed = random.nextInt(5)+1;
        if(sensorManager!=null){
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(accelerometer!=null){
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        balloon.releaseBalloon(mScreenHeight,mSpeed*200 + 3000, tilt );
    }

    /**
     * This method is responsible to pause music if user leave game during gameplay.
     *
     * @see Activity#onPause()
     * @see SoundHelper#pauseMusic()
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mGame) {
            if (mMusic) mMusicHelper.pauseMusic();
        }
    }

    /**
     * This method is responsible to kill the thread after the back button is pressed.
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mGame = false;
        balloonLauncher.cancel(true);
        countDownTimer.cancel();
        tvTime.setText(":0");
        gameOver(true);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            tilt = sensorEvent.values[0]/10;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * This class is responsible for calculating speed of balloons and x axis position of the balloon
     *
     * @see AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {
        /**
         * This method is executing in background and calculate speed and position of balloons depends on game level.
         *
         * @param params represent current level
         * @return null
         * @see AsyncTask#doInBackground(Object[])
         * @see AsyncTask#publishProgress(Object[])
         * @see Thread#sleep(long)
         */
        @Nullable
        @Override
        protected Void doInBackground(@NonNull Integer... params) {
            int minDelay = Math.max(MIN_ANIMATION_DELAY, (MAX_ANIMATION_DELAY - ((params[0] - 1) * 500))) / 2;
            int index = 0;
            while (Integer.parseInt(tvTime.getText().toString().substring(1))>0 && mGame) {
                Random random = new Random(new Date().getTime());
                publishProgress(splitScreenStart[index%3] + random.nextInt((mScreenWidth-200)/5));
                index++;

                try {
                    Thread.sleep(random.nextInt(minDelay/2)+minDelay/4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) break;
            }

            return null;
        }

        /**
         * This method update UI, calling launchBalloon() method.
         *
         * @param values represent calculated x axis of balloon
         * @see GameplayActivity#launchBalloon(int)
         * @see AsyncTask#onProgressUpdate(Object[])
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(mBalloons.size()<10)
                launchBalloon(values[0]);
        }
    }

    /**
     * This method is responsible to start the timer when game starts and to update it after popping 10 balloon .
     *
     */
    private void startTimer(int time){
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(":" + (int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                gameOver(false); //End the game or do whatever you want.
            }
        }.start();
    }
}