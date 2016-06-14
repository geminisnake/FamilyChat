package com.alienleeh.familychat.emotions.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/9..Hour:16
 * Email:alienleeh@foxmail.com
 * Description:表情具体分组 的 滑动页面适配器
 */
public class EmotionPagesAdapter extends PagerAdapter{
    List<GridView> gridViews;
    public EmotionPagesAdapter(List<GridView> emotionViews) {
        gridViews = emotionViews;
    }

    @Override
    public int getCount() {
        return gridViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView gridView = gridViews.get(position);
        container.addView(gridView);
        return gridView;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
