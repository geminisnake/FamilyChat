package com.alienleeh.familychat.helper;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AlienLee on 2016/7/1.
 */
public class UserUpdateHelper {
    private static final String TAG = UserUpdateHelper.class.getSimpleName();

    /**
     * 更新用户资料
     */
    public static void update(final UserInfoFieldEnum field, final Object value, RequestCallbackWrapper<Void> callback) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(field, value);
        update(fields, callback);
    }

    private static void update(final Map<UserInfoFieldEnum, Object> fields, final RequestCallbackWrapper<Void> callback) {
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {

                if (code == ResponseCode.RES_SUCCESS) {
                    //can put log.
                } else {
                    if (exception != null) {
                        //can put log.
                    }
                }
                if (callback != null) {
                    callback.onResult(code, result, exception);
                }
            }
        });
    }
}
