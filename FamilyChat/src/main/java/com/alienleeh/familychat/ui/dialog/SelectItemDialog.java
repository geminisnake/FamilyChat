package com.alienleeh.familychat.ui.dialog;


import android.content.Context;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class SelectItemDialog extends android.app.AlertDialog{
    protected SelectItemDialog(Context context) {
        super(context);
    }
    public static void showDialog(Context context,OnSelectListener listener){

    }

    public interface OnSelectListener {

    }
}
