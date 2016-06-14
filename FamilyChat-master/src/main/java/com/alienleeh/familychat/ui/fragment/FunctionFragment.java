package com.alienleeh.familychat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.helper.LogoutHelper;
import com.alienleeh.familychat.manager.ActivitiesFinisher;
import com.alienleeh.familychat.ui.activity.EditInfoActivity;
import com.alienleeh.familychat.ui.activity.LoginActivity;
import com.alienleeh.familychat.ui.activity.SetAvatarActivity;
import com.alienleeh.familychat.ui.dialog.StandardDialog;
import com.alienleeh.familychat.utils.ActivityUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.alienleeh.mylibrary.customUI.CircleImageView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class FunctionFragment extends BaseFragment {

    private Button bt_function_exit;
    private LinearLayout item_func_personal;
    private CircleImageView civ_entrance_setavatar;
    private TextView mynick_functionpage;
    private TextView myinfo_functionpage;
    private TextView mysign_functionpage;
    private ImageView mygender_functionpage;
    private NimUserInfo myInfo;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function,container,false);
        bt_function_exit = (Button) view.findViewById(R.id.bt_function_exit);
        item_func_personal = (LinearLayout) view.findViewById(R.id.item_func_personal);
        civ_entrance_setavatar = (CircleImageView) view.findViewById(R.id.civ_entrance_setavatar);
        mynick_functionpage = (TextView) view.findViewById(R.id.mynick_functionpage);
        myinfo_functionpage = (TextView) view.findViewById(R.id.myinfo_functionpage);
        mysign_functionpage = (TextView) view.findViewById(R.id.mysign_functionpage);
        mygender_functionpage = (ImageView) view.findViewById(R.id.mygender_functionpage);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void initListener() {
        bt_function_exit.setOnClickListener(this);
        item_func_personal.setOnClickListener(this);
        civ_entrance_setavatar.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setMyInfo();
    }


    public void setMyInfo() {
        myInfo = UserInfoCache.getInstance().getMyInfo();
        if (myInfo != null) {
            ImageLoaderHelper.displayAvatar(myInfo, civ_entrance_setavatar);
            String nickName = myInfo.getName();
            mynick_functionpage.setText(TextUtils.isEmpty(nickName) ? myInfo.getAccount() : nickName);
            myinfo_functionpage.setText(myInfo.getExtension());
            switch (myInfo.getGenderEnum()) {
                case MALE:
                    mygender_functionpage.setImageResource(R.drawable.male);
                    mygender_functionpage.setVisibility(View.VISIBLE);
                    break;
                case FEMALE:
                    mygender_functionpage.setImageResource(R.drawable.female);
                    mygender_functionpage.setVisibility(View.VISIBLE);
                    break;
                default:
                    mygender_functionpage.setVisibility(View.GONE);
                    break;
            }
            mysign_functionpage.setText(myInfo.getSignature());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.custom_menu2,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_power_off:
                StandardDialog.showDialog(getContext(), "确认退出", "您将结束此程序，关闭所有页面。确定吗？", new StandardDialog.StandardDialogListener() {
                    @Override
                    public void onCancel() {
                        ToastUtils.showToast(getContext(),"谢谢~明智之选");
                    }

                    @Override
                    public void onConfirm() {
                        ActivitiesFinisher.finishAll();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.bt_function_exit:
                LogoutHelper.logout();
                ActivityUtils.startActivity(getActivity(), LoginActivity.class,true);
                break;
            case R.id.item_func_personal:
                ActivityUtils.startActivity(getActivity(), EditInfoActivity.class);
                break;
            case R.id.civ_entrance_setavatar:
                String path = myInfo.getAvatar();
                ActivityUtils.startActivity(getActivity(), SetAvatarActivity.class,path);
                break;
        }
    }
}