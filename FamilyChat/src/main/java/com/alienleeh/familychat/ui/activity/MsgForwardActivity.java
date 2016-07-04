package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.VideoListAdapter;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/19..Hour:09
 * Email:alienleeh@foxmail.com
 * Description:转发消息Activity
 */
public class MsgForwardActivity extends BaseActivity{
    @Override
    public void initView() {
        final IMMessage message = (IMMessage) getIntent().getSerializableExtra("msg");
        setContentView(R.layout.activity_just_one_listview);
        ListView userList = (ListView) findViewById(R.id.lisview_in_just);
        List<NimUserInfo> infos = UserInfoCache.getInstance().getAllUsersOfMyFriend();
        final VideoListAdapter adapter = new VideoListAdapter(this,infos);
        userList.setAdapter(adapter);

        AdapterView.OnItemClickListener longClickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        IMMessage fmsg = MessageBuilder.createForwardMessage(message,adapter.getAccount(position), SessionTypeEnum.P2P);
                        NIMClient.getService(MsgService.class).sendMessage(fmsg,false);
                        finish();
                    }
                };
        userList.setOnItemClickListener(longClickListener);
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void processClick(View view) {

    }

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent(context,MsgForwardActivity.class);
        intent.putExtra("msg",message);
        context.startActivity(intent);
    }
}
