package com.alien.sms.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alien.sms.R;
import com.alien.sms.adapter.MyPagerAdapter;
import com.alien.sms.base.BaseActivity;
import com.alien.sms.ui.fragment.ConversationFragment;
import com.alien.sms.ui.fragment.GroupFragment;
import com.alien.sms.ui.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity  {
	private ViewPager viewPager;
	private TextView tvb_group;
	private TextView tvb_conversation;
	private TextView tvb_search;
	private LinearLayout ll_conversation;
	private LinearLayout ll_search;
	private LinearLayout ll_group;
	private View view_scroll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		tvb_conversation = (TextView) findViewById(R.id.tvb_conversation);
		tvb_group = (TextView) findViewById(R.id.tvb_group);
		tvb_search = (TextView) findViewById(R.id.tvb_search);
		ll_conversation = (LinearLayout) findViewById(R.id.ll_conversation);
		ll_group = (LinearLayout) findViewById(R.id.ll_group);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
	}

	@Override
	public void initListener() {
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				textHighLightScale();
			}	

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				int distance = positionOffsetPixels / 3;
				view_scroll.animate().translationX(distance+position * view_scroll.getWidth()).setDuration(0);			
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		ll_conversation.setOnClickListener(this);
		ll_group.setOnClickListener(this);
		ll_search.setOnClickListener(this);
		view_scroll = findViewById(R.id.view_scroll);
	}

	protected void textHighLightScale() {

		int item = viewPager.getCurrentItem();

		tvb_conversation.setTextColor(item == 0? Color.WHITE : 0xff888888);
		tvb_group.setTextColor(item == 1? Color.WHITE : 0xff888888);
		tvb_search.setTextColor(item == 2? Color.WHITE : 0xff888888);		
		
		tvb_conversation.animate().scaleX(item == 0? 1.3F : 1).setDuration(200);
		tvb_group.animate().scaleX(item == 1? 1.3F : 1).setDuration(200);
		tvb_search.animate().scaleX(item == 2? 1.3F : 1).setDuration(200);
		tvb_conversation.animate().scaleY(item == 0? 1.3F : 1).setDuration(200);
		tvb_group.animate().scaleY(item == 1? 1.3F : 1).setDuration(200);
		tvb_search.animate().scaleY(item == 2? 1.3F : 1).setDuration(200);
	}

	@Override
	public void initData() {
		List<Fragment> frList = new ArrayList<>();
		
		ConversationFragment conversationFragment = new ConversationFragment();
		GroupFragment groupFragment = new GroupFragment();
		SearchFragment searchFragment = new SearchFragment();
		
		frList.add(conversationFragment);
		frList.add(groupFragment);
		frList.add(searchFragment);
		viewPager.setOffscreenPageLimit(2);
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), frList);
		viewPager.setAdapter(adapter);
		textHighLightScale();
		computeIndicateLine();

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_conversation:
			viewPager.setCurrentItem(0);
			break;
		case R.id.ll_group:
			viewPager.setCurrentItem(1);
			break;
		case R.id.ll_search:
			viewPager.setCurrentItem(2);
			break;
		}
	}
	@SuppressWarnings("deprecation")
	private void computeIndicateLine(){
		int width = getWindowManager().getDefaultDisplay().getWidth();
			
		view_scroll.getLayoutParams().width = width / 3;
	}
}
