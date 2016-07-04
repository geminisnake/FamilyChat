package com.alienleeh.familychat.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
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
import com.alienleeh.familychat.adapter.ConversationPageAdapter;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.helper.SystemMsgHelper;
import com.alienleeh.familychat.ui.activity.ConversationActivity;
import com.alienleeh.familychat.ui.activity.SystemMsgActivity;
import com.alienleeh.familychat.utils.ToastUtils;
import com.alienleeh.mylibrary.customUI.CircleImageView;
import com.jauker.widget.BadgeView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class MessageFragment extends  BaseFragment{

    private ConversationPageAdapter adapter;
    private ListView listView;
    private List<RecentContact> contactList = new ArrayList<>();
    private LinearLayout ll_system_msg;
    private CircleImageView systemmsg_civ;
    private int unread;
    private BadgeView badgeView;
    private Observer<Integer> observer;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_conversation,container,false);

        setHasOptionsMenu(true);
        ll_system_msg = (LinearLayout) view.findViewById(R.id.ll_system_msg);
        systemmsg_civ = (CircleImageView) view.findViewById(R.id.systemmsg_civ);
        listView = (ListView) view.findViewById(R.id.lv_in_msg_fragment);
        registerObserver(true);
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
                adapter.notifyDataSetChanged();
                ToastUtils.showToast(getContext(),contactList.size()+"");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecentContact();

    }

    @Override
    public void onResume() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        if (observer == null){
            observer = new Observer<Integer>() {
                @Override
                public void onEvent(Integer integer) {
                    if (badgeView != null){
                        badgeView.setBadgeCount(integer);
                    }
                }

            };
        }
        SystemMsgHelper.registerSysUnreadCount(observer,true);

        super.onResume();
    }

    @Override
    public void onPause() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,SessionTypeEnum.None);

        if (observer != null){
            SystemMsgHelper.registerSysUnreadCount(observer,false);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {

        registerObserver(false);
        super.onDestroy();
    }

    private void registerObserver(boolean register) {
        MsgServiceObserve msgServiceObserve = NIMClient.getService(MsgServiceObserve.class);
        msgServiceObserve.observeRecentContact(recentContactObserver,register);

    }
    private Observer<List<RecentContact>> recentContactObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            int index;
            for (RecentContact contact : recentContacts){
                index = -1;
                for (int i = 0;i < contactList.size();i++){
                    if (contact.getContactId().equals(contactList.get(i).getContactId())
                            & contact.getSessionType().equals(contactList.get(i).getSessionType())){
                        index = i;
                        break;
                    }
                }
                if (index >= 0){
                    contactList.remove(index);
                }
                contactList.add(contact);
            }
            notifyDataChange();
        }
    };


    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String accId = adapter.getItem(position).getContactId();
                ConversationActivity.start(getContext(),accId);
            }
        });
        ll_system_msg.setOnClickListener(this);
    }

    @Override
    public void initData() {
        unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        badgeView = new BadgeView(getContext());
        badgeView.setTargetView(systemmsg_civ);
        badgeView.setBadgeCount(unread);
        badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.BOTTOM);

        adapter = new ConversationPageAdapter(getContext(), contactList);
        listView.setAdapter(adapter);
    }

    private void initRecentContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int i, List<RecentContact> recentContacts, Throwable throwable) {
                        if (i != 200 || recentContacts == null){
                            showMessage();
                            return;
                        }
                        contactList.clear();
                        contactList.addAll(recentContacts);
                        if (isAdded()){
                            notifyDataChange();
                        }
                    }
                });
    }

    private void notifyDataChange() {
        Collections.sort(contactList,comp);
        adapter.notifyDataSetChanged();
    }

    private Comparator<? super RecentContact> comp = new Comparator<RecentContact>() {
        @Override
        public int compare(RecentContact lhs, RecentContact rhs) {
            long l = lhs.getTime() - rhs.getTime();
            return l == 0? 0 : (l > 0? -1 : 1);
        }
    };

    private void showMessage() {
        ToastUtils.showToast(getContext(),"还没有消息");
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.ll_system_msg:
                SystemMsgActivity.start(getContext());
        }
    }
}