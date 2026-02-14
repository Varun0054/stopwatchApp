package com.blackspider.stopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AnalogStopwatch extends View {

    private Paint paint;
    private RectF rectF;

    private float seconds = 0;
    private float minutes = 0;

    public AnalogStopwatch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY) - 20;

        // Draw the outer circle
        paint.setColor(Color.parseColor("#1E1E1E"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the minute ticks
        paint.setStrokeWidth(5);
        for (int i = 0; i < 60; i++) {
            float angle = (float) (i * 6 * Math.PI / 180);
            float startX = (float) (centerX + (radius - 10) * Math.sin(angle));
            float startY = (float) (centerY - (radius - 10) * Math.cos(angle));
            float stopX = (float) (centerX + radius * Math.sin(angle));
            float stopY = (float) (centerY - radius * Math.cos(angle));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // Draw the hour numbers
        paint.setTextSize(30);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 1; i <= 12; i++) {
            float angle = (float) (i * 30 * Math.PI / 180);
            float x = (float) (centerX + (radius - 40) * Math.sin(angle));
            float y = (float) (centerY - (radius - 40) * Math.cos(angle) + 10);
            canvas.drawText(String.valueOf(i * 5), x, y, paint);
        }

        // Draw seconds hand
        float secondsAngle = seconds * 6;
        paint.setColor(Color.parseColor("#00BCD4"));
        paint.setStrokeWidth(8);
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, -90, secondsAngle, false, paint);

        // Draw minutes hand
        float minutesAngle = minutes * 6;
        paint.setColor(Color.parseColor("#FF4081"));
        paint.setStrokeWidth(8);
        rectF.set(centerX - radius + 30, centerY - radius + 30, centerX + radius - 30, centerY + radius - 30);
        canvas.drawArc(rectF, -90, minutesAngle, false, paint);
    }

    public void setTime(long updatedTime) {
        seconds = (updatedTime / 1000) % 60;
        minutes = ((updatedTime / 1000) / 60) % 60;
        invalidate();
    }
}
