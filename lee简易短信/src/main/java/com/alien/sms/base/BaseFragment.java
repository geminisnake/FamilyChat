package com.alien.sms.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.alien.sms.utils.LogUtils;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.e(this,"onCreateView");
		return initView(inflater, container, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		initData();
	}
	public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	public abstract void initListener();
	public abstract void initData();
	public abstract void processClick(View v);
	
	@Override
	public void onClick(View v) {
		processClick(v);	
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.e(this,"onStart");
	}

	@Override
	public void onResume() {
		LogUtils.e(this,"onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		LogUtils.e(this,"onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		LogUtils.e(this,"onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		LogUtils.e(this,"onDestroy");
		super.onDestroy();
	}

	@Override
	public void onAttach(Context context) {
		LogUtils.e(this,"onAttach");
		super.onAttach(context);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		LogUtils.e(this,"onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		LogUtils.e(this,"onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		LogUtils.e(this,"onCreate");
		super.onCreate(savedInstanceState);
	}
}
