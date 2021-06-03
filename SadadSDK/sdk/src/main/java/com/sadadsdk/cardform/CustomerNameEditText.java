package com.sadadsdk.cardform;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.sufalamtech.sadad.sdk.R;

import com.sadadsdk.utils.ErrorEditText;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class CustomerNameEditText extends ErrorEditText {

    public CustomerNameEditText(Context context) {
        super(context);
        init();
    }

    public CustomerNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerNameEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        InputFilter[] filters = {new InputFilter.LengthFilter(255)};
        setFilters(filters);
    }

    @Override
    public boolean isValid() {
        return isOptional() || !getText().toString().trim().isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_customer_name_required);
    }
}
