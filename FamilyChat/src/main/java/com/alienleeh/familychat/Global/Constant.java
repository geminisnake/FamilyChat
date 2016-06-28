package com.alienleeh.familychat.Global;

import android.net.Uri;

import com.alienleeh.familychat.Provider.AccountProvider;
import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class Constant {
    public interface UserInfo{
        String[] HEAD = {"昵称","性别","生日","年龄"};
        String[] MIDDLE = {"所在地","手机","邮箱"};
        String[] sign = {"签名"};
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

    public interface AvatarList {
        int[] RES1 = {R.drawable.role_1,R.drawable.role_2,R.drawable.role_3,R.drawable.role_4,R.drawable.role_5,R.drawable.gancong,
                R.drawable.head_icon_1,R.drawable.head_icon_2,R.drawable.head_icon_3,R.drawable.head_icon_4,
                R.drawable.head_icon_5,R.drawable.head_icon_6,R.drawable.head_icon_7,R.drawable.head_icon_8,R.drawable.head_icon_9,R.drawable.head_icon_10,
                R.drawable.xxy003,R.drawable.xxy011,R.drawable.xxy022,R.drawable.xxy035};
    }
}