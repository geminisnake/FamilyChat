package com.alienleeh.familychat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.VideoListAdapter;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.cache.FriendCache;
import com.alienleeh.familychat.ui.dialog.LoadingDialog;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class VideoFragment extends BaseFragment{

    private ListView listView;
    public List<NimUserInfo> userList;
    private LinearLayout bt_add_newfr;
    private LinearLayout bt_add_newgroup;
    private LinearLayout bt_add_hongbao;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videomode,container,false);
        setHasOptionsMenu(true);
        listView = (ListView) view.findViewById(R.id.lv_videomode_friendlist);
        bt_add_newfr = (LinearLayout) view.findViewById(R.id.bt_add_newfr);
        bt_add_newgroup = (LinearLayout) view.findViewById(R.id.bt_add_newgroup);
        bt_add_hongbao = (LinearLayout) view.findViewById(R.id.bt_add_hongbao);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.custom_menu1,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_it_addToDesk:
                ToastUtils.showToast(getContext(),"已添加至桌面");
                return true;
            case R.id.menu_it_autosearch:
                LoadingDialog.getInstance(getContext()).showDialog("进行中",true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initListener() {
        bt_add_newfr.setOnClickListener(this);
        bt_add_newgroup.setOnClickListener(this);
        bt_add_hongbao.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initListData();
    }

    public void initListData() {
        VideoListAdapter adapter = new VideoListAdapter(getContext(), FriendCache.getInstance().getFriendData());
        listView.setAdapter(adapter);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_newfr:

                break;
            case R.id.bt_add_newgroup:

                break;
            case R.id.bt_add_hongbao:

                break;
        }
    }
}
