package com.alien.sms.base;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener{

	protected BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}
	public abstract void processClick(View view);
	public abstract void initView();	
	public abstract void initListener();
	public abstract void initData();
	public void onClick(View view){
		processClick(view);
	}
}
