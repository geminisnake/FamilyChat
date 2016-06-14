package com.alienleeh.familychat.helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.ui.activity.SystemMsgActivity;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

/**
 * Created by AlienLeeH on 2016/7/6.
 */
public class NotificationHelper {
    NotificationManager notificationManager;
    private static NotificationHelper instance;
    Context context;

    private NotificationHelper() {
        init();
    }

    private void init() {
        context = ApplicationUtils.getContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    public static synchronized NotificationHelper getInstance(){
        if (instance == null){
            instance = new NotificationHelper();
        }
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifiSysMsg(String ticker, SystemMessage message) {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.avchat_ring);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.test2)
                .setSound(sound)
                .setTicker(ticker);
        if (message.getType() == SystemMessageType.AddFriend){
            AddFriendNotify notify = (AddFriendNotify) message.getAttachObject();
            switch (notify.getEvent()){
                case RECV_ADD_FRIEND_VERIFY_REQUEST:
                    builder = builder.setContentTitle(message.getFromAccount()+" 请求加你为好友")
                    .setContentText("验证消息"+message.getContent());
                    break;
                case RECV_AGREE_ADD_FRIEND:
                    builder = builder.setContentTitle(message.getFromAccount()+" 同意了你的好友请求")
                    .setContentText("你可以在好友列表进行聊天了");
                    break;
                case RECV_REJECT_ADD_FRIEND:
                    builder.setContentTitle(message.getFromAccount()+" 拒绝了你的好友请求");
                    break;
            }
        }
        Intent in = new Intent(context, SystemMsgActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,in,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        notificationManager.notify(1,notification);
    }
}
