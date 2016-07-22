package com.alienleeh.familychat.customUI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/7/7.
 */
public class MyActionBar extends RelativeLayout{
    public MyActionBar(Context context,String title) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.actionbar_conversation_acti,this);
        LinearLayout actionbar_conver_iconback = (LinearLayout) findViewById(R.id.actionbar_conver_iconback);
        ((TextView)findViewById(R.id.actionbar_title_conversation)).setText(title);
        actionbar_conver_iconback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
