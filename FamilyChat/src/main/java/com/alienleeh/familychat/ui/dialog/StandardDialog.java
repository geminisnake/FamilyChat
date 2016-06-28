package com.alienleeh.familychat.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseDialog;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class StandardDialog extends BaseDialog{

    private TextView tv_dialog_title;
    private TextView tv_dialog_message;
    private Button bt_cancel;
    private Button bt_confirm;
    private String title;
    private String text;
    private StandardDialogListener listener;

    protected StandardDialog(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_standard);
        tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
        bt_cancel = (Button) findViewById(R.id.bt_cancel_standarddialog);
        bt_confirm = (Button) findViewById(R.id.bt_confirm_standarddialog);
    }

    @Override
    public void initListener() {
        bt_cancel.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv_dialog_title.setText(title);
        tv_dialog_message.setText(text);
    }
    public static void showDialog(Context context,String title,String text,StandardDialogListener listener){
        StandardDialog dialog = new StandardDialog(context);
        dialog.title = title;
        dialog.text = text;
        dialog.listener = listener;
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancel_standarddialog:
                listener.onCancel();
                break;
            case R.id.bt_confirm_standarddialog:
                listener.onConfirm();
                break;
        }
        dismiss();
    }
    public interface StandardDialogListener{
        void onCancel();
        void onConfirm();
    }
}
