package com.sadadsdk.cardform;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

import java.util.Arrays;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class CardNumberTransformation implements TransformationMethod {

    private static final String FOUR_DOTS = "••••";

    @Override
    public CharSequence getTransformation(final CharSequence source, View view) {
        if (source.length() >= 9) {
            StringBuilder result = new StringBuilder()
                    .append(FOUR_DOTS)
                    .append(" ")
                    .append(source.subSequence(source.length() - 4, source.length()));

            char[] padding = new char[source.length() - result.length()];
            Arrays.fill(padding, Character.MIN_VALUE);
            result.insert(0, padding);

            return result.toString();
        }

        return source;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {}
}
