package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class UserInfoEditAdapter extends BaseAdapter{
    List<String> list = new ArrayList<String>();
    private LayoutInflater inflater;
    private String item;

    public UserInfoEditAdapter(Context context, String[] head) {
        inflater = LayoutInflater.from(context);
        for (String s : head){
            list.add(s);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_userinfo_detail,parent,false);
        }
        UserInfoHolder holder = getHolder(convertView);
        item = getItem(position).toString();
        if (item.equals("性别") | item.equals("生日") | item.equals("所在地")){
            holder.iv_icon_userinfo.setVisibility(View.VISIBLE);
        }else {
            holder.iv_icon_userinfo.setVisibility(View.INVISIBLE);
        }
        holder.tv_head_userinfo.setText(getItem(position).toString());
        return convertView;
    }
    private UserInfoHolder getHolder(View view){
        UserInfoHolder holder = (UserInfoHolder) view.getTag();
        if (holder == null){
            holder = new UserInfoHolder(view);
            view.setTag(holder);
        }
        return holder;
    }
    class UserInfoHolder{
        TextView tv_head_userinfo;
        EditText et_body_userinfo;
        ImageView iv_icon_userinfo;
        public UserInfoHolder(View view) {
            tv_head_userinfo = (TextView) view.findViewById(R.id.tv_head_userinfo);
            et_body_userinfo = (EditText) view.findViewById(R.id.et_body_userinfo);
            iv_icon_userinfo = (ImageView) view.findViewById(R.id.iv_icon_userinfo);
        }
    }
}
