package com.alienleeh.familychat.emotions.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.MyFragment;
import com.alienleeh.familychat.emotions.adapter.EmotionPagesAdapter;
import com.alienleeh.familychat.emotions.util.EmotionUtils;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.ui.activity.ConversationActivity;
import com.alienleeh.familychat.ui.activity.FileBrowserActivity;
import com.alienleeh.familychat.ui.activity.MapPickActivity;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.mylibrary.customUI.CircleIndicator;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/9..Hour:21
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class FunctionPage extends MyFragment{


    private ViewPager viewPager;
    private CircleIndicator indicator;
    EmotionLayout.SendMsgListener sendMsgListener;
    private String sessionId;


    public void setmEditText(EditText mEditText) {
        this.mEditText = mEditText;
    }

    private EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_emotion_collection,container,false);
        initView(rootView);
        initFunction();
        initListener();
        return rootView;
    }

    private void initListener() {
    }

    private void initView(View rootView) {
        args = getArguments();
        sessionId = args.getString("sessionId");
        viewPager = (ViewPager) rootView.findViewById(R.id.emotion_item_looppager);
        indicator = (CircleIndicator) rootView.findViewById(R.id.emotion_indicator);
        indicator.setViewPager(viewPager);

    }
    private void initFunction(){
        int screenWidth = ApplicationUtils.getWidth();

        int spacing = ApplicationUtils.dp2px(15);

        int itemWidth = (screenWidth - spacing * 5) / 4;

        int gvHeight = itemWidth * 2 + spacing * 3;

        List<GridView> functionViews = new ArrayList<>();
        List<String> functionNames = new ArrayList<>();
        for (String functionname : EmotionUtils.FUCTIONS.keySet()) {
            functionNames.add(functionname);
            if (functionNames.size() == 8){
                GridView gridView = createFunctionGridview(functionNames,screenWidth,itemWidth,spacing,gvHeight);
                functionViews.add(gridView);
                functionNames = new ArrayList<>();
            }
        }

        if (functionNames.size() > 0){
            GridView gridView = createFunctionGridview(functionNames,screenWidth,itemWidth,spacing,gvHeight);
            functionViews.add(gridView);
        }
        EmotionPagesAdapter adapter = new EmotionPagesAdapter(functionViews);
        viewPager.setAdapter(adapter);
    }

    private GridView createFunctionGridview(List<String> functionNames, int screenWidth, int itemWidth, int spacing, int gvHeight) {
        GridView gridView = new GridView(getContext());
        gridView.setSelector(android.R.color.transparent);
        gridView.setNumColumns(4);
        gridView.setPadding(spacing,spacing,spacing,spacing);
        gridView.setVerticalSpacing(spacing);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, gvHeight);
        gridView.setLayoutParams(params);
        FunctionsAdapter adapter = new FunctionsAdapter(getContext(),functionNames,itemWidth);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MapPickActivity.startActivityForResult(getActivity(),ConversationActivity.LOCATION_REQUEST);

                        break;
                    case 1:
                        AndroidImagePicker.getInstance().pickMulti(getActivity(), true, new AndroidImagePicker.OnImagePickCompleteListener() {
                            @Override
                            public void onImagePickComplete(List<ImageItem> items) {
                                for (ImageItem item : items) {
                                    IMMessage message = MessageBuilder.createImageMessage(sessionId, SessionTypeEnum.P2P,new File(item.path));
                                    sendMsgListener.sendMsg(message);
                                }
//                                mEditText.setText(sb.toString());
                            }
                        });
                        return;
                    case 2:
                        FileBrowserActivity.startActivityForResult(getActivity(), ConversationActivity.FILE_REQUEST);
                        break;
                    default:
                        break;
                }

            }
        });
        return gridView;
    }

    public void setMsgSendListener(EmotionLayout.SendMsgListener sendMsgListener) {
        this.sendMsgListener = sendMsgListener;
    }

    private class FunctionsAdapter extends BaseAdapter{
        Context context;
        List<String> functionNames;
        int itemWidth;
        LayoutInflater inflater;
        public FunctionsAdapter(Context context, List<String> functionNames, int itemWidth) {
            this.context = context;
            this.functionNames = functionNames;
            this.itemWidth = itemWidth;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return functionNames.size();
        }

        @Override
        public String getItem(int position) {
            return functionNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_function_inputpanel,parent,false);
//            view.setPadding(itemWidth / 8,itemWidth / 8,itemWidth / 8,itemWidth / 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth,itemWidth);
            view.setLayoutParams(new AbsListView.LayoutParams(itemWidth, AbsListView.LayoutParams.WRAP_CONTENT));
            ImageView imageView = (ImageView) view.findViewById(R.id.function_item);
            imageView.setLayoutParams(params);
            ImageLoaderHelper.displayAsset(imageView,EmotionUtils.FUCTIONS.get(getItem(position)));
            ((TextView)view.findViewById(R.id.function_name)).setText(getItem(position));
            return view;
        }
    }
}
