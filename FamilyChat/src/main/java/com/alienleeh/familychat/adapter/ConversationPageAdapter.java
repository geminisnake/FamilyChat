package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class ConversationPageAdapter extends BaseAdapter{
    private List<NimUserInfo> list;
    private LayoutInflater inflater;
    private View view;
    private MsgViewHolder holder;
    private Drawable drawable;

    public ConversationPageAdapter(Context context,List<NimUserInfo> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        drawable = context.getResources().getDrawable(R.drawable.head_icon_male);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lvmsg_conversation, parent, false);
        }
        holder = getHolder(convertView);
        NimUserInfo nimUserInfo = (NimUserInfo) getItem(position);
        holder.tv_conversation_address.setText(nimUserInfo.getAvatar());
        holder.tv_conversation_body.setText(nimUserInfo.getAccount());
        if (TextUtils.isEmpty(nimUserInfo.getAvatar())){
            holder.iv_conversation_avatarphoto.setImageDrawable(drawable);
        }
        return convertView;
    }

    private MsgViewHolder getHolder(View view){
        MsgViewHolder holder = (MsgViewHolder) view.getTag();
        if (holder == null){
            holder = new MsgViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }
    class MsgViewHolder{
        private TextView tv_conversation_address;
        private TextView tv_conversation_body;
        private ImageView iv_conversation_avatarphoto;
        private TextView tv_conversation_date;

        public MsgViewHolder(View view) {
            tv_conversation_address =  (TextView) view.findViewById(R.id.tv_conversation_address);
            tv_conversation_body =  (TextView) view.findViewById(R.id.tv_conversation_body);
            iv_conversation_avatarphoto = (ImageView) view.findViewById(R.id.iv_conversation_avatarphoto);
            tv_conversation_date = (TextView) view.findViewById(R.id.tv_conversation_date);
        }
    }
}
