package com.alienleeh.familychat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.manager.ActivitiesFinisher;
import com.alienleeh.familychat.ui.activity.EditInfoActivity;
import com.alienleeh.familychat.ui.activity.LoginActivity;
import com.alienleeh.familychat.ui.activity.SetAvatarActivity;
import com.alienleeh.familychat.ui.dialog.StandardDialog;
import com.alienleeh.familychat.utils.ActivityUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class FunctionFragment extends BaseFragment {

    private Button bt_function_exit;
    private LinearLayout item_func_personal;
    private CircleImageView civ_entrance_setavatar;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function,container,false);
        bt_function_exit = (Button) view.findViewById(R.id.bt_function_exit);
        item_func_personal = (LinearLayout) view.findViewById(R.id.item_func_personal);
        civ_entrance_setavatar = (CircleImageView) view.findViewById(R.id.civ_entrance_setavatar);
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
                StandardDialog.showDialog(getContext(), "确认退出", "您确定要干掉此程序吗？手下留情啊~", new StandardDialog.StandardDialogListener() {
                    @Override
                    public void onCancel() {
                        ToastUtils.showToast(getContext(),"谢谢~明智之选");
                    }

                    @Override
                    public void onConfirm() {
                        NIMClient.getService(AuthService.class).logout();
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
                NIMClient.getService(AuthService.class).logout();
                Intent exitIt = new Intent(getContext(),LoginActivity.class);
                exitIt.putExtra("from", Constant.INTENT.FROM_BT_EXIT);
                startActivity(exitIt);
                getActivity().finish();
                break;
            case R.id.item_func_personal:
                ActivityUtils.startActivity(getActivity(), EditInfoActivity.class);
                break;
            case R.id.civ_entrance_setavatar:
                ActivityUtils.startActivity(getActivity(), SetAvatarActivity.class);
                break;
        }
    }
}