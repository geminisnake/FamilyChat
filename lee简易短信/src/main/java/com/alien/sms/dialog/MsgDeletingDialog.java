package com.alien.sms.dialog;

import com.alien.sms.R;
import com.alien.sms.base.BaseDialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MsgDeletingDialog extends BaseDialog {

	private TextView tv;
	private Button bt;
	private ProgressBar pb;
	private int maxProgress;
	private OnDeletingCancelListener cancelListener;
	protected MsgDeletingDialog(Context context,int maxProgress,OnDeletingCancelListener cancelListener) {
		super(context);
		this.maxProgress = maxProgress;
		this.cancelListener = cancelListener;
	}
	public static MsgDeletingDialog showDialog(Context context,int maxProgress,OnDeletingCancelListener cancelListener){
		MsgDeletingDialog deletingDialog = new MsgDeletingDialog(context, maxProgress, cancelListener);
		deletingDialog.show();
		return deletingDialog;
	}
	@Override
	public void processClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bt_determine_deleting:
			if (cancelListener != null) {
				cancelListener.onCancel();
			}
			dismiss();
			break;
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tv.setText("正在删除(0/"+maxProgress+")");
		pb.setMax(maxProgress);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		bt.setOnClickListener(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.dialog_msg_deleting);
		tv = (TextView) findViewById(R.id.tv_deleting_dialog_title);
		bt = (Button) findViewById(R.id.bt_determine_deleting);
		pb = (ProgressBar) findViewById(R.id.pb_determine_deleting);
	}
	public interface OnDeletingCancelListener{
		void onCancel();
	}
	public void updateProgressAndTitle(int progress){
		pb.setProgress(progress);
		tv.setText("正在删除("+progress+"/"+maxProgress+")");
	}
}
