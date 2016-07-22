package com.alienleeh.familychat.customUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by AlienLeeH on 2016/7/11..Hour:14
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class MsgTextView extends TextView{
    public MsgTextView(Context context) {
        super(context);
        setTextSize(18);
        setGravity(Gravity.CENTER);
    }

    public MsgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MsgTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
