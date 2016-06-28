package com.alienleeh.familychat.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.SyncDataStatusObserver;
import com.alienleeh.familychat.adapter.SimPagerAdapter;
import com.alienleeh.familychat.adapter.SlideAdapter;
import com.alienleeh.familychat.cache.FriendCache;
import com.alienleeh.familychat.customUI.MyViewPager;
import com.alienleeh.familychat.dao.SimQueryHandler;
import com.alienleeh.familychat.manager.ActivitiesFinisher;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.ui.dialog.LoadingDialog;
import com.alienleeh.familychat.ui.fragment.FunctionFragment;
import com.alienleeh.familychat.ui.fragment.MessageFragment;
import com.alienleeh.familychat.ui.fragment.VideoFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.netease.nimlib.sdk.Observer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

        boolean syncCompleted = SyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {

            public void onEvent(Void v) {
                LoadingDialog.getInstance(MainActivity.this).disappear();
                if (videoFragment == null){
                    videoFragment = new VideoFragment();
                }
                if (videoFragment.userList == null){
                    videoFragment.userList = FriendCache.getInstance().getFriendData();
                    videoFragment.initListData();
                }
            }
        });
        if (!syncCompleted){
            LoadingDialog.getInstance(MainActivity.this).showDialog("加载中",false);
        }
        initView();
        initActionBar();
        initSlidingMenu();
        initLisenter();
        initData();
        NIMClientManager.initOnlineStatusObserver();

        ActivitiesFinisher.addActivity(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        initViewPager();
        SlideAdapter adapter = new SlideAdapter(this,null,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv_slidingmenu.setAdapter(adapter);
        SimQueryHandler handler = new SimQueryHandler(getContentResolver());
        handler.startQuery(0,adapter, Constant.URI.ACCOUNTINFO_QUERY,new String[]{"account_id","password","_id"},null,null,null);
//        main_vpager.setCanScroll(true);
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
        FunctionFragment functionFragment = new FunctionFragment();

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

