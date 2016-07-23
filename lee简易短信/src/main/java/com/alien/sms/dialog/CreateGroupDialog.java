package com.alien.sms.dialog;

import com.alien.sms.R;
import com.alien.sms.base.BaseDialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateGroupDialog extends BaseDialog {

	private TextView tv_title;
	private EditText et_type;
	private Button bt_cancel;
	private Button bt_confirm;
	private String title;
	private OnCreateConfirmListener listener;

	protected CreateGroupDialog(Context context,String title,OnCreateConfirmListener confirmListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.listener = confirmListener;
	}

	@Override
	public void processClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bt_et_dialog_cancel:
			if (listener != null) {
				listener.onCancel();
			}
			break;
		case R.id.bt_et_confirm_dialog:
			if (listener != null) {
				listener.onConfirm(et_type.getText().toString());
			}
			break;
		}
		dismiss();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tv_title.setText(title);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		bt_cancel.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.dialog_group_create);	
		tv_title = (TextView) findViewById(R.id.tv_et_dialog_title);
		et_type = (EditText) findViewById(R.id.et_dialog_type_something);
		bt_cancel = (Button) findViewById(R.id.bt_et_dialog_cancel);
		bt_confirm = (Button) findViewById(R.id.bt_et_confirm_dialog);
	}
	public static void showDialog(Context context,String title,OnCreateConfirmListener confirmListener){
		CreateGroupDialog dialog = new CreateGroupDialog(context,title,confirmListener);
		//对话框不支持文本输入，手动设置
		dialog.setView(new EditText(context));
		dialog.show();
		dialog.et_type.clearFocus();
	}
	public interface OnCreateConfirmListener{
		void onCancel();
		void onConfirm(String string);
	}
}
