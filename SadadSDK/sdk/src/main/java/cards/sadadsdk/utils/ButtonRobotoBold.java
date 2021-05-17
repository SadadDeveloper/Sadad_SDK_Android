package cards.sadadsdk.utils;

/**
 * Created by yama on 7/11/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.sufalamtech.sadad.sdk.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class ButtonRobotoBold extends AppCompatButton {

    public ButtonRobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ButtonRobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonRobotoBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {

            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
            setTypeface(tf);

        }
    }
}
