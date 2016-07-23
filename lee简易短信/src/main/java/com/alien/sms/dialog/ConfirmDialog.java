package com.alien.sms.dialog;

import com.alien.sms.R;
import com.alien.sms.base.BaseDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends BaseDialog {
	private String title;
	private String message;
	private Button cancel;
	private Button confirm;
	private TextView tv_dialog_title;
	private TextView tv_dialog_message;
	private OnConfirmListener confirmListener;
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOnConfirmListener(OnConfirmListener confirmListener) {
		this.confirmListener = confirmListener;
	}
	
	protected ConfirmDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	}
	@Override
	public void processClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bt_confirm_cancel:
			if (confirmListener != null) {
				confirmListener.onCancel();
			}
			break;
		case R.id.bt_confirm_delete:
			if (confirmListener != null) {
				confirmListener.onConfirm();
			}		
			break;
		}dismiss();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tv_dialog_title.setText(title);
		tv_dialog_message.setText(message);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	@Override
	public void initView() {	
		setContentView(R.layout.dialog_msg_delete);
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
		cancel = (Button) findViewById(R.id.bt_confirm_cancel);
		confirm = (Button) findViewById(R.id.bt_confirm_delete);
	}
	public static void showDialog(Context context,String title,String message,OnConfirmListener onConfirmListener){
		ConfirmDialog dialog = new ConfirmDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setOnConfirmListener(onConfirmListener);
		dialog.show();
	}
	public interface OnConfirmListener{
		void onCancel();
		void onConfirm();
	}
}
