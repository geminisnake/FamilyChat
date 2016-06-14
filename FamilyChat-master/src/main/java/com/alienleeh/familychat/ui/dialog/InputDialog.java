package com.alienleeh.familychat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/7/6.
 */
public class InputDialog extends Dialog{

    private EditText et1_inputdialog;
    private EditText et2_inputdialog;
    private Button bt_inputdialog;
    private TextView inputdialog_title;
    private String title;
    private String hint1;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getDialog_title() {
        return inputdialog_title;
    }

    private ProgressBar progressBar;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHint1(String hint1) {
        this.hint1 = hint1;
    }

    public void setHint2(String hint2) {
        this.hint2 = hint2;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private String hint2;

    private View.OnClickListener clickListener;


    public EditText getEt1_inputdialog() {
        return et1_inputdialog;
    }

    public EditText getEt2_inputdialog() {
        return et2_inputdialog;
    }

    public InputDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_edit);
        et1_inputdialog = (EditText) findViewById(R.id.et1_inputdialog);
        et2_inputdialog = (EditText) findViewById(R.id.et2_inputdialog);
        et1_inputdialog.setHint(hint1);
        et2_inputdialog.setHint(hint2);
        progressBar = (ProgressBar) findViewById(R.id.progress_inputdialog);
        inputdialog_title = (TextView) findViewById(R.id.inputdialog_title);
        bt_inputdialog = (Button) findViewById(R.id.bt_inputdialog);
        inputdialog_title.setText(title);
        bt_inputdialog.setOnClickListener(clickListener);
    }
}
