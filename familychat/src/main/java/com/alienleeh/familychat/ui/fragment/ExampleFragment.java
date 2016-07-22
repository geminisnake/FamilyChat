package com.alienleeh.familychat.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alienleeh.familychat.base.MyFragment;

import java.util.Random;

/**
 * Created by AlienLeeH on 2016/7/9..Hour:13
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class ExampleFragment extends MyFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        Random random =  new Random();
        view.setGravity(Gravity.CENTER);
        args = getArguments();
        String text = args.getString("text");
        view.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        view.setText(text);
        view.setTextColor(Color.WHITE);
        view.setTextSize(48);
        return view;
    }
}
