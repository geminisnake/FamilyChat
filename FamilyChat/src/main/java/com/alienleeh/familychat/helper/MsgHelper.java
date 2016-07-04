package com.alienleeh.familychat.helper;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/19..Hour:14
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class MsgHelper {
    public static void queryExFirst(String accountId, RequestCallbackWrapper<List<IMMessage>> callback) {
        IMMessage anchor = MessageBuilder.createEmptyMessage(accountId, SessionTypeEnum.P2P,0);
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,50,true).setCallback(callback);
    }

    public static void pullToRefresh(String accountId, IMMessage message, RequestCallbackWrapper<List<IMMessage>> refreshCallback) {
        if (message == null){
            IMMessage anchor = MessageBuilder.createEmptyMessage(accountId, SessionTypeEnum.P2P,0);
            NIMClient.getService(MsgService.class).pullMessageHistory(anchor,50,true).setCallback(refreshCallback);
            return;
        }
        NIMClient.getService(MsgService.class).pullMessageHistory(message,15,false).setCallback(refreshCallback);
    }
}
