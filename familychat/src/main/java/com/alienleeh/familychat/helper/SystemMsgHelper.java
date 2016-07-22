package com.alienleeh.familychat.helper;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

/**
 * Created by AlienLeeH on 2016/7/6.
 */
public class SystemMsgHelper {
    private static Observer<SystemMessage> sysmsgobserver;

    public static void registerSystemMsgReceived(boolean register){
        if (register){
            if (sysmsgobserver == null){
                sysmsgobserver = new Observer<SystemMessage>() {
                    @Override
                    public void onEvent(SystemMessage systemMessage) {
                        NotificationHelper.getInstance().sendNotifiSysMsg("您有一条新的系统通知",systemMessage);
                    }
                };
            }
            NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(sysmsgobserver,true);
        }else {
            if (sysmsgobserver != null){
                NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(sysmsgobserver,false);
            }
        }
    }
    public static void registerSysUnreadCount(Observer<Integer> observer,boolean register){
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(observer,register);
    }
}
