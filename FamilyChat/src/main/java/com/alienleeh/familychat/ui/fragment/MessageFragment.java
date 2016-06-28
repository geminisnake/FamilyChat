package com.alienleeh.familychat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.ConversationPageAdapter;
import com.alienleeh.familychat.base.BaseFragment;
import com.alienleeh.familychat.cache.FriendCache;
import com.alienleeh.familychat.utils.ToastUtils;


/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class MessageFragment extends  BaseFragment{

    private ConversationPageAdapter adapter;
    private ListView listView;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_conversation,container,false);

        setHasOptionsMenu(true);
        listView = (ListView) view.findViewById(R.id.lv_in_msg_fragment);
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
                ToastUtils.showToast(getContext(),"开始搜索");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addRecentContactsFragment();
    }

    private void addRecentContactsFragment() {
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        adapter = new ConversationPageAdapter(getContext(), FriendCache.getInstance().getFriendData());
        listView.setAdapter(adapter);
    }

    @Override
    public void processClick(View v) {

    }
}