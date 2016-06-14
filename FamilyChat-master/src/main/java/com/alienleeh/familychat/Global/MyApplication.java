package com.alienleeh.familychat.Global;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.bean.LoginSetting;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.ui.activity.MainActivity;
import com.alienleeh.familychat.utils.SharePreferenceUtils;
import com.alienleeh.familychat.utils.SystemUtil;
import com.baidu.mapapi.SDKInitializer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by AlienLeeH on 2016/6/23.
 */
public class MyApplication extends Application{

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        mainThreadId = android.os.Process.myTid();
        NIMClient.init(this,logininfo(),options());

        if (inMainProcess()){
            NIMClientManager.init(context);
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
            NIMClient.toggleNotification(true);
            SDKInitializer.initialize(context);
        }

        super.onCreate();
    }



    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private LoginInfo logininfo() {
        LoginSetting setting = SharePreferenceUtils.loadSetting();
        if (setting.isAutoLogin()){
            NIMClientManager.setAccount(setting.getDefaultAccount());
//        暂时不用加密算了，测试麻烦    String mpassword = MD5.getStringMD5();
            return new LoginInfo(setting.getDefaultAccount(),setting.getPassword());
        }
        return null;
    }

    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.smile;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.dddddddddefault;
            }

            @Override
            public Bitmap getTeamIcon(String s) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

}
