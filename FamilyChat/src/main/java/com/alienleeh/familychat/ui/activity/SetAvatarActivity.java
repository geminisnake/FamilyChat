package com.alienleeh.familychat.ui.activity;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.GridViewAdapter;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.ui.dialog.SelectItemDialog;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class SetAvatarActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView gv1_avatar_acti;
    private GridView gv2_avatar_acti;
    private CircleImageView avataractivity_set_default;
    private LinearLayout actionbar_setavatar_iconback;

    @Override
    public void initView() {
        setContentView(R.layout.activity_setup_avatar);
        gv1_avatar_acti = (GridView) findViewById(R.id.gv1_avatar_acti);
        gv2_avatar_acti = (GridView) findViewById(R.id.gv2_avatar_acti);
        avataractivity_set_default = (CircleImageView) findViewById(R.id.avataractivity_set_default);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_setavatar_acti);
            actionbar_setavatar_iconback = (LinearLayout) findViewById(R.id.actionbar_setavatar_iconback);
        }
    }

    @Override
    public void initListener() {
        gv1_avatar_acti.setOnItemClickListener(this);
        actionbar_setavatar_iconback.setOnClickListener(this);
    }

    @Override
    public void initData() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i : Constant.AvatarList.RES1){
            list.add(i);
        }
        gv1_avatar_acti.setAdapter(new GridViewAdapter(this,list));

    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.avataractivity_set_default:
                SelectItemDialog.showDialog(this,new SelectItemDialog.OnSelectListener(){

                });
                break;
            case R.id.actionbar_setavatar_iconback:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
