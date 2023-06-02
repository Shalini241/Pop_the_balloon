package com.pop_up_balloon.objects;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

/**
 *
 * <p>Class for the square balloon game object.</p>
 */
public class SquareBalloon extends Balloon{

    public SquareBalloon(Context context) {
        super(context);
    }

    /**
     *
     * <p>Constructor for the square balloon game object.</p>
     */
    public SquareBalloon(@NonNull Context context, int color, int rawHeight) {
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
         canvas.drawRect(rawHeight,rawHeight,rawHeight - param,
                rawHeight- param,balloonPaint);
    }
}
