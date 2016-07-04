package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.jauker.widget.BadgeView;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/6.
 */
public class TitleMsgAdapter extends BaseAdapter{
    LayoutInflater inflater;
    List<SystemMessage> messages;
    Context context;
    public TitleMsgAdapter(Context context, List<SystemMessage> msgs) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        messages = msgs;
    }

    @Override
    public int getCount() {
        if (messages != null){
            return messages.size();
        }
        return 0;
    }

    @Override
    public SystemMessage getItem(int position) {
        if (messages != null && !messages.isEmpty()){
            return messages.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_title_msg,parent,false);
        }
        EasyHolder holder = getHolder(convertView);
        SystemMessage message = getItem(position);
        holder.time.setText(DateFormat.getDateFormat(context).format(message.getTime()));
        if (message.getType() == SystemMessageType.AddFriend){
            AddFriendNotify notify = (AddFriendNotify) message.getAttachObject();
            switch (notify.getEvent()){
                case RECV_ADD_FRIEND_VERIFY_REQUEST:
                    holder.title.setText(String.format("账号%s用户请求添加你为好友", message.getFromAccount()));
                    holder.msg.setText(String.format("验证消息：%s", message.getContent()));
                    holder.msg.setVisibility(View.VISIBLE);
                    break;
                case RECV_AGREE_ADD_FRIEND:
                    holder.title.setText(String.format("账号%s用户通过了你的好友请求", message.getFromAccount()));
                    holder.msg.setVisibility(View.GONE);
                    break;
                case RECV_REJECT_ADD_FRIEND:
                    holder.title.setText(String.format("账号%s用户拒绝了你的好友请求", message.getFromAccount()));
                    holder.msg.setVisibility(View.GONE);
            }
            BadgeView badgeView = new BadgeView(context);
            badgeView.setTargetView(holder.title);
            badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.BOTTOM);
            if (message.isUnread()){
                badgeView.setBadgeCount(1);
            }else {
                badgeView.setBadgeCount(0);
            }

        }
        return convertView;
    }

    private EasyHolder getHolder(View v){
        EasyHolder holder = (EasyHolder) v.getTag();
        if (holder == null){
            holder = new EasyHolder(v);
            v.setTag(holder);
        }
        return holder;
    }
    class EasyHolder{
        TextView title;
        TextView msg;
        TextView time;

        public EasyHolder(View v) {
            this.title = (TextView) v.findViewById(R.id.title_title_msg);
            this.msg = (TextView) v.findViewById(R.id.msg_title_msg);
            time = (TextView) v.findViewById(R.id.time_title_msg);
        }
    }
}
