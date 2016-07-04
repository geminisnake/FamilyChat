package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.mylibrary.customUI.CircleImageView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/27.
 */
public class VideoListAdapter extends BaseAdapter{
    private List<NimUserInfo> list;
    LayoutInflater inflater;
    public VideoListAdapter(Context context,List<NimUserInfo> nimUserInfos) {
        inflater = LayoutInflater.from(context);
        list = nimUserInfos;
    }

    @Override
    public int getCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public NimUserInfo getItem(int position) {
        return list.get(position);
    }
    public String getAccount(int position){
        return getItem(position).getAccount();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_list_videochat,parent,false);
        }
        NimUserInfo info = getItem(position);
        VListViewHolder holder = getHolder(convertView);
        ImageLoaderHelper.displayAvatarList(info.getAvatar(),holder.civ_item_video);
        if (TextUtils.isEmpty(info.getName())){
            holder.tv_name_videochat.setText(info.getAccount());
        }else {
            holder.tv_name_videochat.setText(info.getName());
        }

        return convertView;
    }

    private VListViewHolder getHolder(View view){
        VListViewHolder holder = (VListViewHolder) view.getTag();
        if (holder == null){
            holder = new VListViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }
    class VListViewHolder{
        CircleImageView civ_item_video;
        TextView tv_name_videochat;
        TextView tv_nettype_videochat;
        ImageView iv_client_type_videochat;
        public VListViewHolder(View view) {
            civ_item_video = (CircleImageView) view.findViewById(R.id.civ_item_video);
            tv_name_videochat = (TextView) view.findViewById(R.id.tv_name_videochat);
            tv_nettype_videochat = (TextView) view.findViewById(R.id.tv_nettype_videochat);
            iv_client_type_videochat = (ImageView) view.findViewById(R.id.iv_client_type_videochat);
        }
    }
}
