package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.emotions.util.SpanStringUtils;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class ConversationPageAdapter extends BaseAdapter{
    private List<RecentContact> list;
    private LayoutInflater inflater;
    private View view;
    Context context;
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public ConversationPageAdapter(Context context, List<RecentContact> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public RecentContact getItem(int position) {
        return list.get(position);
    }

    public String getName(int position){
        String account = list.get(position).getContactId();
        return UserInfoCache.getInstance().getUserName(account);
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
        MsgViewHolder holder = getHolder(convertView);
        RecentContact contact = getItem(position);
        NimUserInfo info = UserInfoCache.getInstance().getUserInfo(contact.getContactId());
        if (info != null){
            String name = UserInfoCache.getInstance().getUserName(contact.getContactId());
            holder.tv_conversation_address.setText(name);
            ImageLoaderHelper.displayAvatarList(TextUtils.isEmpty(info.getAvatar())? "defaultavatar.png" : info.getAvatar(), holder.iv_conversation_avatarphoto);
        }else {
            ImageLoaderHelper.displayAvatarList("defaultavatar.png",holder.iv_conversation_avatarphoto);
            holder.tv_conversation_address.setText(contact.getContactId());
        }

        holder.tv_conversation_body.setText(SpanStringUtils.getEmotionContent(context,holder.tv_conversation_body,contact.getContent()));
        if (DateUtils.isToday(contact.getTime())){
            holder.tv_conversation_date.setText(DateFormat.getTimeFormat(convertView.getContext()).format(contact.getTime()));
        }else {
            holder.tv_conversation_date.setText(DateFormat.getDateFormat(convertView.getContext()).format(contact.getTime()));
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
