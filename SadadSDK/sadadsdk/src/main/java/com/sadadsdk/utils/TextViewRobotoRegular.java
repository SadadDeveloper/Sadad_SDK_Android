package com.sadadsdk.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.sadadsdk.R;


/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class TextViewRobotoRegular extends AppCompatTextView {

    public TextViewRobotoRegular(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRobotoRegular(Context context) {
        super(context);
        init();
    }

    /**
     * this will initialize the type face and assign it to the textview
     */
    private void init() {
        if (!isInEditMode()) {
//            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
//                    Constant.FONT_ROBOTO_REGULAR);
//            setTypeface(tf);

            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
            setTypeface(tf);
        }
    }
}
