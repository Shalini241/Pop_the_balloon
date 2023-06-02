package com.pop_up_balloon.objects;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
/**
 *
 * <p>Class for the circle balloon game object.</p>
 */
public class CircleBalloon extends Balloon{

    public CircleBalloon(Context context) {
        super(context);
    }

    /**
     *
     * <p>Constructor for the square balloon game object.</p>
     */
    public CircleBalloon(@NonNull Context context, int color, int rawHeight) {
        super(context, color, rawHeight);
    }

    /**
     *
     * <p>This method takes canvas as parameter and draws square balloon</p>
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the balloon
        canvas.drawCircle(param/2,param/2, param/2, balloonPaint);
    }
}
