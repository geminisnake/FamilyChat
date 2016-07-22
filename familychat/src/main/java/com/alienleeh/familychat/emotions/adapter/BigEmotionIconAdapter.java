package com.alienleeh.familychat.emotions.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alienleeh.familychat.emotions.util.EmotionUtils;
import com.alienleeh.familychat.helper.ImageLoaderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/9..Hour:16
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class BigEmotionIconAdapter extends BaseAdapter{
    private Context context;
    private int itemWidth;
    List<String> emotionPaths;
    List<String> emotionNames;



    public BigEmotionIconAdapter(Context context, List<String> emotionNames,int itemWidth,int emotionType) {
        this.itemWidth = itemWidth;
        this.context = context;
        this.emotionNames = emotionNames;
        emotionPaths = new ArrayList<>();
        for (String emotionName : emotionNames) {

            emotionPaths.add(EmotionUtils.getImgByName(emotionType, emotionName));
        }
    }

    @Override
    public int getCount() {
        return emotionPaths.size();
    }

    @Override
    public String getItem(int position) {
        return emotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = new ImageView(context);
        // 设置内边距
        view.setPadding(itemWidth/8, itemWidth/8, itemWidth/8, itemWidth/8);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth,itemWidth);
        view.setLayoutParams(params);
        ImageLoaderHelper.displayAsset(view,emotionPaths.get(position));
        return view;
    }
}
