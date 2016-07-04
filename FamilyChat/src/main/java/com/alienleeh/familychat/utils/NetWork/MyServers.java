package com.alienleeh.familychat.utils.NetWork;

/**
 * Created by AlienLeeH on 2016/7/4.
 */
public class MyServers {
    public static final String API_SERVER_TEST = "https://api.netease.im/nimserver/user/create.action"; // 测试
    private static final String API_SERVER = "https://app.netease.im/api/"; // 线上

    public static String apiServer() {
        return API_SERVER;
    }
}
