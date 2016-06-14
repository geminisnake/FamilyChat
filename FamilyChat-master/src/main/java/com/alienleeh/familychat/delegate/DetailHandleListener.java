package com.alienleeh.familychat.delegate;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by AlienLeeH on 2016/7/19..Hour:00
 * Email:alienleeh@foxmail.com
 * Description:消息内容点击接口
 */
public interface DetailHandleListener {
    void onContainerLongClick(IMMessage message);
}
