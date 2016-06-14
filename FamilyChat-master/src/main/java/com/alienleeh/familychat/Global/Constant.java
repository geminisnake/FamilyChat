package com.alienleeh.familychat.Global;

import android.net.Uri;

import com.alienleeh.familychat.Provider.AccountProvider;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class Constant {
    public interface UserInfo{
        String[] UPDATE_LIST = {"昵称","性别","生日","年龄","所在地","手机","邮箱","签名"};
        int GENDER_MALE = 0;
        int GENDER_FEMALE = 1;
    }
    public interface UI{

    }
    public interface INTENT{
        int FROM_BT_EXIT = 10;
    }

    public interface URI{
        Uri ACCOUNTINFO_QUERY = Uri.withAppendedPath(AccountProvider.BASE_URI,"/userinfo/query");
        Uri ACCOUNTINFO_INSERT = Uri.withAppendedPath(AccountProvider.BASE_URI,"/userinfo/insert");
        Uri ACCOUNTINFO_UPDATE = Uri.withAppendedPath(AccountProvider.BASE_URI,"/userinfo/update");
        Uri ACCOUNTINFO_DELETE = Uri.withAppendedPath(AccountProvider.BASE_URI,"/userinfo/delete");
    }
    public interface IPconnection{
        String SEVER_HOST = "192.168.14.101";
        int PORT = 5222;
        int LOGIN_SUCCESS = 101;
        int LOGIN_FAILED = 404;
    }

    public interface CACHE {
        String SHAREPRE_LOGIN = "LoginCache_SharePre";
    }


}