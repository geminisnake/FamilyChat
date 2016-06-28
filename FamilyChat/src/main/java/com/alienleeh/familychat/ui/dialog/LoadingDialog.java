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
    private static LoadingDialog dialog = null;
    private String message;
    private TextView msg;

    private LoadingDialog(Context context) {
        super(context);
    }
    public static synchronized LoadingDialog getInstance(Context context){
        if (dialog == null){
            dialog = new LoadingDialog(context);
        }
        return dialog;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        msg = (TextView) findViewById(R.id.loading_dialog_message);
        msg.setText(message);
    }
    public void showDialog(String msg,boolean cancelable){
        dialog.setMessage(msg);
        dialog.setCancelable(cancelable);
        dialog.show();
    }
    public void disappear(){
        dialog.dismiss();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
