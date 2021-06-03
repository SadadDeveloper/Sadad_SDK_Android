package com.sadadsdk.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.sufalamtech.sadad.sdk.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class TextViewRobotoBold extends AppCompatTextView {

    public TextViewRobotoBold(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRobotoBold(Context context) {
        super(context);
        init();
    }

    /**
     * this will initialize the type face and assign it to the textview
     */
    private void init() {
        if (!isInEditMode()) {
//            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
//                    Constant.FONT_ROBOTO_BOLD);
//            setTypeface(tf);
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
            setTypeface(tf);
        }
    }

    /*This is where the magic happens*/
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        float offset = getTextSize() - getLineHeight();
//        canvas.translate(0, offset);
//        super.onDraw(canvas);
//    }
}
