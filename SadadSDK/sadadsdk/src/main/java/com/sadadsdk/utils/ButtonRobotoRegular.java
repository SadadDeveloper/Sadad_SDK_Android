package com.sadadsdk.utils;

/**
 * Created by Hitesh on 7/11/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.sadadsdk.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class ButtonRobotoRegular extends AppCompatButton {

    public ButtonRobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ButtonRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonRobotoRegular(Context context) {
        super(context);
        init();
    }


    private void init() {
        if (!isInEditMode()) {
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
            setTypeface(tf);
        }
    }
}
