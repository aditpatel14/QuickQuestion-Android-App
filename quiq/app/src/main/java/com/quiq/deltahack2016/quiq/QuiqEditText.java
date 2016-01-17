package com.quiq.deltahack2016.quiq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Vanshil on 2016-01-17.
 */
public class QuiqEditText extends EditText {


    public QuiqEditText(Context context) {
        super(context);
    }

    public QuiqEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuiqEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
