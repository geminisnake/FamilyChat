package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.mylibrary.customUI.CircleImageView;

import java.io.IOException;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class GridViewAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private String[] pathes;

    public GridViewAdapter(Context context) {
        loadPaths(context);
    }

    private void loadPaths(Context context){
        inflater = LayoutInflater.from(context);
        AssetManager manager = context.getAssets();
        try {
            pathes = manager.list("avatars");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (pathes == null){
            return 0;
        }
        return pathes.length;
    }

    @Override
    public String getItem(int position) {
        return pathes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private GridAvatarHolder getHolder(View view){
        GridAvatarHolder holder = (GridAvatarHolder) view.getTag();
        if (holder == null){
            holder = new GridAvatarHolder(view);
            view.setTag(holder);
        }
        return holder;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_gridview_avatar,parent,false);
        }
        GridAvatarHolder holder = getHolder(convertView);
        ImageLoaderHelper.displayAvatarList(getItem(position),holder.civ_system_avatar_item);

        return convertView;
    }
    class GridAvatarHolder{
        CircleImageView civ_system_avatar_item;
        RelativeLayout rl_avatar_item_tosetwidth;
        public GridAvatarHolder(View view) {
            rl_avatar_item_tosetwidth = (RelativeLayout) view.findViewById(R.id.rl_avatar_item_tosetwidth);
            civ_system_avatar_item = (CircleImageView) view.findViewById(R.id.civ_avatar_item);
        }
    }
}
