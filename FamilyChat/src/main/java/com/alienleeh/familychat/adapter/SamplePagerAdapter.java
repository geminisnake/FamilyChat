package com.alienleeh.familychat.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by AlienLeeH on 2016/7/9.01
 * Email:alienleeh@foxmail.com
 * Description:测试界面展示adapter
 */
public class SamplePagerAdapter extends PagerAdapter {
    Random random = new Random();
    private int mSize;

    @Override
    public int getCount() {
        return mSize;
    }

    public SamplePagerAdapter() {
        mSize = 5;
    }

    public SamplePagerAdapter(int n) {
        if (n >= 0) {
            mSize = n;
        } else {
            mSize = 2;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        TextView textView = new TextView(view.getContext());
        textView.setText(String.valueOf(position + 1));
        textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(48);
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return textView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
