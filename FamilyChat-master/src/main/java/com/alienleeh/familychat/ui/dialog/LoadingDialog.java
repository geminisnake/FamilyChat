package com.alienleeh.familychat.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/6/26.
 */
public class LoadingDialog extends AlertDialog{
    private String shortmessage;
    private TextView msg;

    public LoadingDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        msg = (TextView) findViewById(R.id.loading_dialog_message);
        msg.setText(shortmessage);
    }
    public void showDialog(String msg,boolean cancelable,boolean cancelTouch){
        this.setMessage(msg);
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(cancelTouch);
        this.show();
    }
    public void disappear(){
        this.dismiss();
    }

    public void setshortmessage(String message) {
        this.shortmessage = message;
    }
}
