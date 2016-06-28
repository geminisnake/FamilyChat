package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.alienleeh.familychat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class GridViewAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<Integer> list;

    public GridViewAdapter(Context context, List<Integer> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
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

        holder.civ_system_avatar_item.setImageResource((Integer) getItem(position));
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
