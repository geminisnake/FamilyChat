package com.alienleeh.familychat.emotions.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.MyFragment;
import com.alienleeh.familychat.emotions.EmotionFragmentFactory;
import com.alienleeh.familychat.emotions.GlobalOnItemClickManagerUtils;
import com.alienleeh.familychat.emotions.adapter.BigEmotionIconAdapter;
import com.alienleeh.familychat.emotions.adapter.EachEmotionItemAdapter;
import com.alienleeh.familychat.emotions.adapter.EmotionPagesAdapter;
import com.alienleeh.familychat.emotions.util.EmotionUtils;
import com.alienleeh.familychat.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class EmotionCollectionFragment extends MyFragment implements View.OnClickListener{


    private ViewPager looppager;
    private CircleIndicator emotion_indicator;
    private int emotionType;
    private EmotionPagesAdapter emotionPagerGvAdapter;
    private boolean scale;
    EmotionLayout.SendMsgListener sendMsgListener;
    private String accountId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_emotion_collection,container,false);
        args = getArguments();
        emotionType = (int) args.get(EmotionFragmentFactory.EMOTION_TYPE);
        scale = args.getBoolean(EmotionFragmentFactory.FONT_SCALE);
        accountId = args.getString(EmotionFragmentFactory.ACCOUNT_ID);
        initView(rootView);

        initListener();
        initData();

        return rootView;
    }

    private void initView(View rootView) {
        looppager = (ViewPager) rootView.findViewById(R.id.emotion_item_looppager);
        emotion_indicator = (CircleIndicator) rootView.findViewById(R.id.emotion_indicator);
        initEmotion(scale);
    }

    private void initEmotion(boolean scale) {
        // 获取屏幕宽度
        int screenWidth = ApplicationUtils.getWidth();

        // item的间距
        int spacing = ApplicationUtils.dp2px(scale? 12 : 15);

        // 动态计算item的宽度和高度
        int itemWidth;
        if (scale){
            itemWidth = (screenWidth - spacing * 8) / 7;
        }else {
            itemWidth = (screenWidth - spacing * 5) / 4;
        }


        //动态计算gridview的总高度
        int gvHeight = scale? itemWidth * 3 + spacing * 6 : itemWidth * 2 + spacing * 3;
        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();

        // 遍历所有的表情的key
        if (scale){
            for (String emojiName : EmotionUtils.getEmotionMap(emotionType).keySet()) {
                emotionNames.add(emojiName);

                if (emotionNames.size() == 20) {
                    GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                    emotionViews.add(gv);

                    //清空重来
                    emotionNames = new ArrayList<>();
                }
            }
        }else {
            for (String emotionname : EmotionUtils.getEmotionMap(emotionType).keySet()) {
                emotionNames.add(emotionname);
                if (emotionNames.size() == 8){
                    GridView gridView = createEmotionGridView(emotionNames,screenWidth,spacing,itemWidth,gvHeight);
                    emotionViews.add(gridView);
                    emotionNames = new ArrayList<>();
                }
            }
        }


        // 处理剩余
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagesAdapter(emotionViews);
        looppager.setAdapter(emotionPagerGvAdapter);

    }

    private GridView createEmotionGridView(List<String> emotionNames, int screenWidth, int pading, int itemWidth, int gvHeight) {
        GridView view = new GridView(getContext());
        view.setSelector(android.R.color.transparent);
        view.setNumColumns(scale? 7 : 4);
        view.setPadding(pading,pading,pading,pading);
        if (scale){
            view.setHorizontalSpacing(pading);
        }else {
            view.setHorizontalSpacing(0);
        }

        view.setBackgroundColor(0xFFADCFC6);
        view.setVerticalSpacing(scale? pading * 2 : pading);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, gvHeight);
        view.setLayoutParams(params);
        BaseAdapter adapter = scale?
                new EachEmotionItemAdapter(getContext(),emotionNames,itemWidth,emotionType)
                : new BigEmotionIconAdapter(getContext(),emotionNames,itemWidth,emotionType);
        view.setAdapter(adapter);
        view.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getContext()).getOnItemClickListener(sendMsgListener,accountId));
        return view;
    }

    public void initListener() {

    }
    
    public void initData() {
        emotion_indicator.setViewPager(looppager);
    }

    @Override
    public void onClick(View v) {

    }

    public void setSendMsgListener(EmotionLayout.SendMsgListener sendMsgListener) {
        this.sendMsgListener = sendMsgListener;
    }
}
