package com.alienleeh.familychat.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alienleeh.familychat.base.BaseDialog;

/**
 * 非常简单的对话框，一行字，一个钮。可以传入资源id来换肤
 * Created by AlienLeeH on 2016/7/1.
 */
public class EasyDialog extends BaseDialog{
    private int ResId;
    private int buttonId = -1;
    private EasyDialogListener listener;
    private Button bt;
    private int textId;
    private TextView tv;
    private String text;

    protected EasyDialog(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        setContentView(ResId);
        bt =  (Button) findViewById(buttonId);
        tv = (TextView) findViewById(textId);
    }

    @Override
    public void initListener() {
        bt.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv.setText(text);
    }

    @Override
    public void processClick(View v) {
        if (v.getId() == buttonId){
            if (listener != null){
                listener.onClick();
            }
            else {
                dismiss();
            }
        }
    }

    public static void showDialog(Context contexts,int[] dialogRes, String text, boolean cancelAble,EasyDialogListener listener) {
        EasyDialog dialog= new EasyDialog(contexts);
        dialog.ResId = dialogRes[0];
        dialog.textId = dialogRes[1];
        dialog.buttonId = dialogRes[2];
        dialog.setCancelable(cancelAble);
        dialog.setCanceledOnTouchOutside(cancelAble);
        if (listener != null){
            dialog.listener = listener;
        }
        dialog.text = text;
        dialog.show();
    }

    public static void showDialog(Context context,int[] dialogRes, String text, boolean cancelAble) {
        showDialog(context,dialogRes,text,cancelAble,null);
    }

    public interface EasyDialogListener{
        void onClick();
    }
}
