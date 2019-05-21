package com.sadadsdk.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class SpaceSpan extends ReplacementSpan {

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        float padding = paint.measureText(" ", 0, 1);
        float textSize = paint.measureText(text, start, end);
        return (int) (padding + textSize);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                     int bottom, Paint paint) {
        canvas.drawText(text.subSequence(start, end) + " ", x, y, paint);
    }
}