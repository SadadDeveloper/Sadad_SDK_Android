package cards.sadadsdk.cardform;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.sufalamtech.sadad.sdk.R;

import cards.sadadsdk.utils.ErrorEditText;


/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class MobileNumberEditText extends ErrorEditText {

    public MobileNumberEditText(Context context) {
        super(context);
        init();
    }

    public MobileNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MobileNumberEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = {new InputFilter.LengthFilter(14)};
        setFilters(filters);
        addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    /**
     * @return the unformatted mobile number entered by the user
     */
    public String getMobileNumber() {
        return PhoneNumberUtils.stripSeparators(getText().toString());
    }

    @Override
    public boolean isValid() {
        return isOptional() || getText().toString().length() >= 8;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_mobile_number_required);
    }
}
