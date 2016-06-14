package com.alienleeh.familychat.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.VideoListAdapter;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.cache.FriendCache;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.ui.activity.UserInfoActiviy;
import com.alienleeh.familychat.ui.dialog.InputDialog;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class VideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private ListView listView;
    public List<NimUserInfo> userList;
    private LinearLayout bt_add_newfr;
    private LinearLayout bt_add_newgroup;
    private LinearLayout bt_add_hongbao;
    private VideoListAdapter adapter;
    private Handler uiHandler;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videomode,container,false);
        setHasOptionsMenu(true);
        listView = (ListView) view.findViewById(R.id.lv_videomode_friendlist);
        bt_add_newfr = (LinearLayout) view.findViewById(R.id.bt_add_newfr);
        bt_add_newgroup = (LinearLayout) view.findViewById(R.id.bt_add_newgroup);
        bt_add_hongbao = (LinearLayout) view.findViewById(R.id.bt_add_hongbao);
        uiHandler = new Handler(getContext().getMainLooper());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swr_frag_video);
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
        swipeRefreshLayout.setOnRefreshListener(this);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String account = adapter.getAccount(position);
                UserInfoActiviy.start(getContext(),account);
            }
        });
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.fruitYellow);
        initListData();
        registerObservers();
    }

    private void registerObservers() {
        FriendCache.FriendDataChangedObserver observer = new FriendCache.FriendDataChangedObserver() {
            @Override
            public void onAddedOrUpdatedFriends(List<String> accounts) {
                List<String> unCacheAcc = new ArrayList<>(1);
                for (String accid : accounts){
                    if (UserInfoCache.getInstance().getUserInfo(accid) == null){
                        unCacheAcc.add(accid);
                    }
                }
                UserInfoCache.getInstance().getUserInfoFromRemote(unCacheAcc, new RequestCallbackWrapper<List<NimUserInfo>>() {
                    @Override
                    public void onResult(int i, List<NimUserInfo> nimUserInfos, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS){
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onDeletedFriends(List<String> accounts) {

            }

            @Override
            public void onAddUserToBlackList(List<String> account) {

            }

            @Override
            public void onRemoveUserFromBlackList(List<String> account) {

            }
        };

        FriendCache.getInstance().registerFriendDataChangedObserver(observer,true);
    }

    public void initListData() {
        adapter = new VideoListAdapter(getContext(), UserInfoCache.getInstance().getAllUsersOfMyFriend());
        listView.setAdapter(adapter);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_newfr:
                final InputDialog dialog = new InputDialog(getContext());
                dialog.setTitle(getString(R.string.add_friend));
                dialog.setHint1(getString(R.string.fri_acc));
                dialog.setHint2(getString(R.string.yanzheng_info));
                dialog.setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.getDialog_title().setVisibility(View.GONE);
                        dialog.getProgressBar().setVisibility(View.VISIBLE);
                        String accid = dialog.getEt1_inputdialog().getText().toString();
                        String info = dialog.getEt2_inputdialog().getText().toString();
                        AddFriendData friendData = new AddFriendData(accid, VerifyType.VERIFY_REQUEST,info);
                        NIMClient.getService(FriendService.class).addFriend(friendData).setCallback(new RequestCallbackWrapper<Void>() {
                            @Override
                            public void onResult(int i, Void aVoid, Throwable throwable) {
                                if (i == 200){
                                    ToastUtils.showToast(getContext(),"请求已发送");
                                }else {
                                    ToastUtils.showToast(getContext(),"发送请求失败");
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.bt_add_newgroup:

                break;
            case R.id.bt_add_hongbao:

                break;
        }
    }

    public void refreshList() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }
}
