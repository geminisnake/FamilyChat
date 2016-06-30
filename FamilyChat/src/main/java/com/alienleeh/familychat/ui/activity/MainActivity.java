package com.alienleeh.familychat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.Observer.SyncDataStatusObserver;
import com.alienleeh.familychat.Observer.UserInfoObservable;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.SimPagerAdapter;
import com.alienleeh.familychat.adapter.SlideAdapter;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.customUI.MyViewPager;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.helper.SimQueryHandler;
import com.alienleeh.familychat.helper.UserInfoHelper;
import com.alienleeh.familychat.manager.ActivitiesFinisher;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.ui.dialog.LoadingDialog;
import com.alienleeh.familychat.ui.dialog.StandardDialog;
import com.alienleeh.familychat.ui.fragment.FunctionFragment;
import com.alienleeh.familychat.ui.fragment.MessageFragment;
import com.alienleeh.familychat.ui.fragment.VideoFragment;
import com.alienleeh.familychat.utils.ToastUtils;
import com.alienleeh.mylibrary.customUI.CircleImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener{

    private ListView lv_slidingmenu;
    private MyViewPager main_vpager;
    private SlidingMenu menu;

    private static final int PAGE_SELECT_VIDEO = 0;
    private static final int PAGE_SELECT_MESSAGE = 1;
    private static final int PAGE_SELECT_FUCTION = 2;

    private LinearLayout ll_videopage;
    private LinearLayout ll_messagepage;
    private LinearLayout ll_functionpage;
    private TextView tv_changeto_functionpage;
    private TextView tv_changeto_messagepage;
    private TextView tv_changeto_videopage;
    private ImageView iv_video;
    private ImageView iv_message;
    private ImageView iv_function;

    private TextView title_myactionbar;
    private CircleImageView logo_myactionbar;
    private VideoFragment videoFragment;
    private FunctionFragment functionFragment;
    private String myAccount;

    public NimUserInfo getMyInfo() {
        return myInfo;
    }

    private NimUserInfo myInfo;


    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_mademyself);
            title_myactionbar = (TextView) findViewById(R.id.title_myactionbar);
            logo_myactionbar = (CircleImageView) findViewById(R.id.logo_myactionbar);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccount = NIMClientManager.getAccount();
        beforeInit();

        initView();
        initActionBar();
        initSlidingMenu();
        initLisenter();
        initData();

        registObserver(true);

        ActivitiesFinisher.addActivity(this);
    }

    //缓存中弹加载对话框
    private void beforeInit() {
        final LoadingDialog dialog = new LoadingDialog(this);
        boolean syncCompleted = SyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {

            public void onEvent(Void v) {

                if (videoFragment == null){
                    videoFragment = new VideoFragment();
                }
                if (videoFragment.userList == null){
                    videoFragment.userList = UserInfoCache.getInstance().getAllUsersOfMyFriend();
                    videoFragment.initListData();
                }
                dialog.disappear();
            }
        });
        if (!syncCompleted){
            dialog.showDialog("加载缓存中",false,false);
        }
    }

    private void registObserver(boolean register) {
        if (register){
            UserInfoHelper.registerObserver(AvatarObserver);
        }else {
            UserInfoHelper.unRegisterObserver(AvatarObserver);
        }
    }
    private UserInfoObservable.UserInfoObserver AvatarObserver = new UserInfoObservable.UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (accounts.contains(myAccount)){
                refreshActionBarAvatar();
                if (functionFragment != null){
                    functionFragment.setMyInfo();
                }
            }
            videoFragment.refreshList();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (main_vpager != null) {
            int currentPage = main_vpager.getCurrentItem();
            if (currentPage != 2) {
                menu.findItem(R.id.menu_it_addToDesk).setVisible(true);
                menu.findItem(R.id.menu_it_autosearch).setVisible(true);
                menu.findItem(R.id.menu_power_off).setVisible(false);
            } else {
                menu.findItem(R.id.menu_it_addToDesk).setVisible(false);
                menu.findItem(R.id.menu_it_autosearch).setVisible(false);
                menu.findItem(R.id.menu_power_off).setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_it_autosearch:

                break;
            case R.id.menu_it_addToDesk:

                break;
            case R.id.menu_power_off:
                StandardDialog.showDialog(this, "确认退出", "您将结束此程序，关闭所有页面。确定吗？", new StandardDialog.StandardDialogListener() {
                    @Override
                    public void onCancel() {
                        ToastUtils.showToast(MainActivity.this,"谢谢~明智之选");
                    }

                    @Override
                    public void onConfirm() {
                        ActivitiesFinisher.finishAll();
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onParseIntent();
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages != null && messages.size() != 0) {
            // 可以获取消息的发送者，跳转到指定的单聊、群聊界面。
            for (IMMessage message : messages){
                if (message.getSessionType() == SessionTypeEnum.P2P){
                    ConversationActivity.start(this,message.getSessionId());
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registObserver(false);
        ActivitiesFinisher.removeActivity(this);
    }

    private void initSlidingMenu() {
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 4);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.sliding_menu_main);
        menu = getSlidingMenu();
        main_vpager = (MyViewPager) findViewById(R.id.main_vpager);
        ll_videopage = (LinearLayout) findViewById(R.id.ll_videopage);
        ll_messagepage = (LinearLayout) findViewById(R.id.ll_messagepage);
        ll_functionpage = (LinearLayout) findViewById(R.id.ll_functionpage);
        tv_changeto_videopage = (TextView) findViewById(R.id.tv_changeto_videopage);
        tv_changeto_messagepage = (TextView) findViewById(R.id.tv_changeto_messagepage);
        tv_changeto_functionpage = (TextView) findViewById(R.id.tv_changeto_functionpage);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        iv_function = (ImageView) findViewById(R.id.iv_function);
        lv_slidingmenu = (ListView) findViewById(R.id.lv_slidingmenu);

    }

    private void initLisenter() {
        ll_videopage.setOnClickListener(this);
        ll_messagepage.setOnClickListener(this);
        ll_functionpage.setOnClickListener(this);
        logo_myactionbar.setOnClickListener(this);
        main_vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onTabPageChange();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        refreshActionBarAvatar();

        initViewPager();
        SlideAdapter adapter = new SlideAdapter(this,null,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv_slidingmenu.setAdapter(adapter);
        SimQueryHandler handler = new SimQueryHandler(getContentResolver());
        handler.startQuery(0,adapter, Constant.URI.ACCOUNTINFO_QUERY,new String[]{"account_id","password","_id"},null,null,null);
//        main_vpager.setCanScroll(true);
    }

    private void refreshActionBarAvatar() {
        myInfo = UserInfoCache.getInstance().getMyInfo();
        if (myInfo != null){
            ImageLoaderHelper.displayAvatar(myInfo,logo_myactionbar);
        }
    }

    @Override
    protected void onResume() {
        onTabPageChange();
        super.onResume();
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        if (videoFragment == null){
            videoFragment = new VideoFragment();
        }
        MessageFragment messageFragment = new MessageFragment();
        functionFragment = new FunctionFragment();

        fragmentList.add(videoFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(functionFragment);

        SimPagerAdapter adapter = new SimPagerAdapter(getSupportFragmentManager(), fragmentList);
        main_vpager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_videopage:
                main_vpager.setCurrentItem(PAGE_SELECT_VIDEO);
                break;
            case R.id.ll_functionpage:
                main_vpager.setCurrentItem(PAGE_SELECT_FUCTION);
                break;
            case R.id.ll_messagepage:
                main_vpager.setCurrentItem(PAGE_SELECT_MESSAGE);
                break;
            case R.id.logo_myactionbar:
                menu.toggle(true);
        }
    }
    protected void onTabPageChange() {
        // 获取ViewPager当前显示的索引
        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);

        int item = main_vpager.getCurrentItem();
        switch (item){
            case 0:
                title_myactionbar.setText(R.string.mode_video_chat);
                logo_myactionbar.setVisibility(View.VISIBLE);
                break;
            case 1:
                title_myactionbar.setText(R.string.mode_text_chat);
                logo_myactionbar.setVisibility(View.VISIBLE);
                break;
            case 2:
                title_myactionbar.setText(R.string.mode_setting);
                logo_myactionbar.setVisibility(View.GONE);
                break;
        }
        iv_video.setBackgroundResource(item == 0? R.drawable.ic_ondemand_video_brown_300_18dp : R.drawable.ic_ondemand_video_brown_200_18dp);
        iv_message.setBackgroundResource(item == 1? R.drawable.ic_message_black_18dp : R.drawable.ic_message_brown_200_18dp);
        iv_function.setBackgroundResource(item == 2? R.drawable.ic_pages_brown_300_18dp : R.drawable.ic_pages_brown_200_18dp);
        tv_changeto_videopage.animate().scaleX(item == 0? 1.5F : 1).setDuration(200);
        tv_changeto_messagepage.animate().scaleX(item == 1? 1.5F : 1).setDuration(200);
        tv_changeto_functionpage.animate().scaleX(item == 2? 1.5F : 1).setDuration(200);
        tv_changeto_videopage.animate().scaleY(item == 0? 1.5F : 1).setDuration(200);
        tv_changeto_messagepage.animate().scaleY(item == 1? 1.5F : 1).setDuration(200);
        tv_changeto_functionpage.animate().scaleY(item == 2? 1.5F : 1).setDuration(200);
        title_myactionbar.setGravity(item == 2? Gravity.LEFT : Gravity.NO_GRAVITY);
    }
}

