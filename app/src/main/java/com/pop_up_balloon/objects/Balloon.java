package com.pop_up_balloon.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import java.util.Random;

import com.pop_up_balloon.activities.GameplayActivity;
import com.pop_up_balloon.utils.PixelHelper;

/**
 * <h1>Balloon</h1>
 *
 * <p><b>Balloon</b> class represent balloon game object.</p>
 */
@SuppressLint("AppCompatCustomView")
public class Balloon extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private ValueAnimator mAnimator;
    private BalloonListener mListener;
    private boolean mPopped;
    protected int param;
    protected int rawHeight;
    protected Paint balloonPaint;

    public int color;

    public Balloon(Context context) {
        super(context);
    }

    /**
     *
     * <p>Constructor for the balloon game object.</p>
     */
    public Balloon(@NonNull Context context, int color, int rawHeight) {
        super(context);
        mListener = (BalloonListener) context;
        this.rawHeight = rawHeight;
        this.color = color;
        Random random = new Random();
        int p = random.nextInt(32)+32;
        param = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                p, context.getResources().getDisplayMetrics());
        balloonPaint = new Paint();
        balloonPaint.setStyle(Paint.Style.FILL);
        balloonPaint.setAntiAlias(true);
        balloonPaint.setStrokeWidth(2f);
        balloonPaint.setColor(color);
        setLayoutParams(new ViewGroup.LayoutParams(PixelHelper.pixelsToDp(rawHeight/2, context), PixelHelper.pixelsToDp(rawHeight/2, context)));
    }

    /**
     * This method is responsible for create releasing balloons animation during gameplay.
     *
     * @param screenHeight represents screen height
     * @param duration     represents number of milliseconds for animation duration. With increasing level, this number is reduces.
     * @see ValueAnimator
     */
    public void releaseBalloon(int screenHeight, int duration, float tilt) {
        mAnimator = ValueAnimator.ofFloat(screenHeight, 0f);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
        mAnimator.addUpdateListener((e)->{
            this.setX(this.getX()+tilt);
        });
    }

    /**
     * This method is calling every time when animation is update. Y axis of balloon is then increasing.
     *
     * @param valueAnimator represent object to animate.
     * @see ValueAnimator#getAnimatedValue()
     * @see ValueAnimator.AnimatorUpdateListener#onAnimationUpdate(ValueAnimator)
     */
    @Override
    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
        setY((float) valueAnimator.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animator) {
    }

    /**
     * This method is called when animation is finished. If user failed to pop balloon, balloon is popped mannualy and user
     * lose one life or lose the game depends on number of life that he has.
     */
    @Override
    public void onAnimationEnd(Animator animator) {
        if (!mPopped) mListener.popBalloon(this, false);
    }

    @Override
    public void onAnimationCancel(Animator animator) {
    }

    @Override
    public void onAnimationRepeat(Animator animator) {
    }

    /**
     * This method is called when user touch the screen. If user pop balloon, method popBalloon is called.
     *
     * @param event represent which action user take.
     * @return boolean which return method onTouchEvent of superclass.
     * @see BalloonListener#popBalloon(Balloon, boolean)
     * @see android.app.Activity#onTouchEvent(MotionEvent)
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!mPopped && event.getAction() == MotionEvent.ACTION_DOWN) {
            mListener.popBalloon(this, true);
            mPopped = true;
            mAnimator.cancel();
        }

        return super.onTouchEvent(event);
    }

    /**
     * This method set state that represent if balloon is popped or not and if it is cancel animation.
     *
     * @param isBalloonPopped indicate if balloon is popped or not.
     * @see Animation#cancel()
     */
    public void setPopped(boolean isBalloonPopped) {
        mPopped = isBalloonPopped;
        if (isBalloonPopped) mAnimator.cancel();
    }

    /**
     * This interface define one method, popBalloon which is implemented by GamePlayActivity.
     *
     * @see GameplayActivity#popBalloon(Balloon, boolean)
     */
    public interface BalloonListener {

        /**
         * @param balloon   represent Balloon object which should be popped.
         * @param userTouch boolean which indicate if user pop balloon or not.
         */
        void popBalloon(Balloon balloon, boolean userTouch);
    }
}