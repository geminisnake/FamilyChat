package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.TitleMsgAdapter;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.helper.SystemMsgHelper;
import com.alienleeh.familychat.ui.dialog.ListDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/6.
 */
public class SystemMsgActivity extends BaseActivity{

    private ListView list_system_msg;
    private LinearLayout actionbar_conver_iconback;
    private TitleMsgAdapter adapter;
    private String[] functions = {"接受","拒绝"};
    @Override
    public void initView() {
        setContentView(R.layout.activity_system_msg);
        list_system_msg = (ListView) findViewById(R.id.list_system_msg);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_conversation_acti);
            actionbar_conver_iconback = (LinearLayout) findViewById(R.id.actionbar_conver_iconback);
            TextView actionbar_title_conversation = (TextView) findViewById(R.id.actionbar_title_conversation);
            actionbar_title_conversation.setText("系统消息");
        }
    }

    @Override
    public void initListener() {
        actionbar_conver_iconback.setOnClickListener(this);
        list_system_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SystemMessage message = adapter.getItem(position);
                if (!message.isUnread()){
                    return;
                }
                message.setUnread(false);
                if (message.getType() == SystemMessageType.AddFriend
                        & ((AddFriendNotify)message.getAttachObject()).getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST){
                    ListDialog.showDialog(SystemMsgActivity.this, "处理好友请求", functions, new ListDialog.OnListDialogListener() {
                        @Override
                        public void OnItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 1){
                                NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(),false);
                            }else {
                                NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(),true).setCallback(new RequestCallbackWrapper<Void>() {
                                    @Override
                                    public void onResult(int i, Void aVoid, Throwable throwable) {
                                        if (throwable != null){
                                            try {
                                                throw new Exception(throwable);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }else if (i == ResponseCode.RES_SUCCESS){
                                            IMMessage imMessage = MessageBuilder.createTextMessage(message.getFromAccount(), SessionTypeEnum.P2P,"我们已经是好友了");
                                            NIMClient.getService(MsgService.class).sendMessage(imMessage,false);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initData() {
        List<SystemMessage> msgs = NIMClient.getService(SystemMessageService.class)
                .querySystemMessagesBlock(0, 50);
        adapter = new TitleMsgAdapter(this,msgs);
        list_system_msg.setAdapter(adapter);

        SystemMsgHelper.registerSysUnreadCount(observer,true);
    }
    private Observer<Integer> observer =  new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            adapter.notifyDataSetChanged();
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
    }

    @Override
    protected void onDestroy() {
        SystemMsgHelper.registerSysUnreadCount(observer,false);
        super.onDestroy();
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.actionbar_conver_iconback:
                finish();
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context,SystemMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
