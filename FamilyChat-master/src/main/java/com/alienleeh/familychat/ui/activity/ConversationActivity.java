package com.alienleeh.familychat.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.ConversationDetailAdapter;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.emotions.fragments.EmotionLayout;
import com.alienleeh.familychat.helper.MsgHelper;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.baidu.mapapi.model.LatLng;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/30.
 */
public class ConversationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final int FILE_REQUEST = 102;
    public static final int PHOTO_REQUEST = 101;
    public static final int LOCATION_REQUEST = 100;
    public static final int MSG_DELETE_ITEM = 801;
    public static final int MSG_FORWARD = 802;
    private LinearLayout actionbar_conver_iconback;
    private ListView msgList;
    private String accountId;
    ConversationDetailAdapter adapter;
    private List<IMMessage> items = new ArrayList<>();
    private Observer<List<IMMessage>> incomingMsgObserver;
    private boolean firstLoad = true;
    private InputMethodManager imm;
    private Observer<IMMessage> msgStatusObserver;
    private Observer<AttachmentProgress> attachmentObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress attachmentProgress) {

            onAttachmentProgress(attachmentProgress);
        }
    };
    private SwipeRefreshLayout refreshLayout;
    private EmotionLayout emotionLayout;
    private FrameLayout detail_container;


    private void onAttachmentProgress(AttachmentProgress attachmentProgress) {
        if (adapter!=null){
            String uuid = attachmentProgress.getUuid();
            int position = -1;
            MsgTypeEnum typeEnum = MsgTypeEnum.undef;
            for (int i = 0;i<items.size();i++){
                if (items.get(i).getUuid().equals(uuid)){
                    position = i;
                    typeEnum = items.get(i).getMsgType();
                    break;
                }
            }
            int first = msgList.getFirstVisiblePosition();
            int last = msgList.getLastVisiblePosition();
            if (position < first || position > last){
                return;
            }
            int progress = (int) (attachmentProgress.getTransferred() * 100 / attachmentProgress.getTotal());
            adapter.notifyProgressChanged(uuid,progress,typeEnum);
        }
    }


    public static void start(Context context, String accountId) {
        Intent sIntent = new Intent(context,ConversationActivity.class);
        sIntent.putExtra("accountId",accountId);
        context.startActivity(sIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case FILE_REQUEST:
                if (resultCode == Activity.RESULT_OK){
                    String path = data.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
                    if (!TextUtils.isEmpty(path)){
                        File file = new File(path);
                        IMMessage message = MessageBuilder.createFileMessage(accountId,SessionTypeEnum.P2P,file,file.getName());
                        NIMClient.getService(MsgService.class).sendMessage(message,false);
                        items.add(message);
                        refreshMsg();
                    }
                }
                break;
            case LOCATION_REQUEST:
                if (resultCode == Activity.RESULT_OK){
                    LatLng location = data.getParcelableExtra("location");
                    IMMessage message = MessageBuilder.createLocationMessage(
                            accountId,
                            SessionTypeEnum.P2P,
                            location.latitude,
                            location.longitude,
                            data.getStringExtra("address"));
                    NIMClient.getService(MsgService.class).sendMessage(message,false);
                    items.add(message);
                    refreshMsg();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerComingMsgObserver();
    }

    @Override
    protected void onResume() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        super.onResume();
    }

    @Override
    protected void onPause() {
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterComingMsgObserver();
    }
    //onDestroy中注销消息接收观察者
    private void unRegisterComingMsgObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver,false);
        if (msgStatusObserver != null){
            NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(msgStatusObserver,false);
        }
        NIMClient.getService(MsgServiceObserve.class).observeAttachmentProgress(attachmentObserver,false);
    }
    //注册消息接收观察者，如果消息发送方来自本界面对话账号account，刷新界面
    private void registerComingMsgObserver() {
        if (incomingMsgObserver == null){
            incomingMsgObserver = new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> imMessages) {
                    if (imMessages == null || imMessages.isEmpty()){
                        return;
                    }
                    boolean needRefresh = false;
                    for (IMMessage newitem : imMessages){
                        if (isMyMsg(newitem)){
                            items.add(newitem);
                            needRefresh = true;
                        }
                    }
                    if (needRefresh){
                        refreshMsg();
                    }
                }
            };
        }
        if (msgStatusObserver == null){
            msgStatusObserver = new Observer<IMMessage>() {
                @Override
                public void onEvent(IMMessage message) {
                    if (isMyMsg(message)){
                        int index = getItemIndex(message.getUuid());
                        if (index >= 0 & index < items.size()){
                            IMMessage imMessage = items.get(index);
                            imMessage.setStatus(message.getStatus());
                            imMessage.setAttachStatus(message.getAttachStatus());
                            if (imMessage.getAttachment() instanceof AVChatAttachment
                                    || imMessage.getAttachment() instanceof AudioAttachment) {
                                imMessage.setAttachment(message.getAttachment());
                            }
                            refreshMsg();
                        }
                    }
                }
            };
        }
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver,true);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(msgStatusObserver,true);
        NIMClient.getService(MsgServiceObserve.class).observeAttachmentProgress(attachmentObserver,true);
    }

    private int getItemIndex(String uuid) {
        for (int i = 0;i<items.size();i++){
            if (TextUtils.equals(items.get(i).getUuid(),uuid)){
                return i;
            }
        }
        return -1;
    }

    private boolean isMyMsg(IMMessage newitem) {
        return newitem.getSessionType() == SessionTypeEnum.P2P
                && newitem.getSessionId() != null
                && newitem.getSessionId().equals(accountId);
    }

    @Override
    public void initView() {
        onParseIntent();
        setContentView(R.layout.activity_conversation_detail);
        msgList = (ListView) findViewById(R.id.lv_conversation_message);
        msgList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        detail_container = (FrameLayout) findViewById(R.id.conver_detail_container);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swr_acti_conversation);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.fruitYellow);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initEmotionLayout();
    }

    private void initEmotionLayout() {
        Bundle bundle = new Bundle();
        bundle.putString("accountId",accountId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transition = fragmentManager.beginTransaction();
        emotionLayout = EmotionLayout.newInstance(EmotionLayout.class,bundle);
        emotionLayout.setContentView(detail_container);
        emotionLayout.setAudioView(findViewById(R.id.layoutPlayAudio));
        emotionLayout.setSendMsgListener(new EmotionLayout.SendMsgListener() {
            @Override
            public void sendMsg(final IMMessage message) {
                NIMClient.getService(MsgService.class).sendMessage(message,false).setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {
                        if (i == ResponseCode.RES_SUCCESS){
                            ToastUtils.showToast(ConversationActivity.this,"success");

                        }
                    }
                });
                items.add(message);
                refreshMsg();
            }
        });
        transition.replace(R.id.input_panel, emotionLayout);
        transition.addToBackStack(null);
        transition.commit();
    }


    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_conversation_acti);
        }

        actionbar_conver_iconback = (LinearLayout) findViewById(R.id.actionbar_conver_iconback);

        ((TextView) findViewById(R.id.actionbar_title_conversation)).setText(UserInfoCache.getInstance().getUserName(accountId));
    }

    @Override
    public void initListener() {
        actionbar_conver_iconback.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        msgList.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
        msgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                emotionLayout.togglePanel();
                return adapter.clearPopup();
            }
        });
    }

    @Override
    public void initData() {
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_DELETE_ITEM:
                        IMMessage m1 = (IMMessage) msg.obj;
                        items.remove(m1);
                        NIMClient.getService(MsgService.class).deleteChattingHistory(m1);
                        refreshMsg(false);
                        break;
                    case MSG_FORWARD:
                        m1 = (IMMessage) msg.obj;
                        MsgForwardActivity.start(ConversationActivity.this,m1);
                        break;
                }
            }
        };
        adapter = new ConversationDetailAdapter(this,items,accountId);
        adapter.setHandler(handler);
        msgList.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgList.setSelection(msgList.getBottom());
            }
        },250);
        msgList.setSelection(msgList.getBottom());
        MsgHelper.queryExFirst(accountId,callback);


    }
    private RequestCallbackWrapper<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
        @Override
        public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
            if (imMessages != null){
                onMessageLoaded(imMessages);
            }
        }
    };
    private RequestCallbackWrapper<List<IMMessage>> refreshCallback = new RequestCallbackWrapper<List<IMMessage>>() {
        @Override
        public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
            if (imMessages != null){
                items.addAll(imMessages);
                Collections.sort(items, new Comparator<IMMessage>() {
                    @Override
                    public int compare(IMMessage m1, IMMessage m2) {
                        long t1 = m1.getTime();
                        long t2 = m2.getTime();
                        return t1 - t2 == 0? 0 : t1 - t2 > 0? 1 : -1;
                    }
                });
                onPullFinish(imMessages.size());

            }

        }
    };

    private void onPullFinish(int size) {
        int x = msgList.getScrollX();
        int y = msgList.getScrollY();
        adapter.notifyDataSetChanged();
        LogUtils.e("position is","add size :"+size);
        msgList.scrollTo(x,y);
//        msgList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        ApplicationUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        },1000);
    }

    private void onMessageLoaded(List<IMMessage> imMessages) {
        items.addAll(imMessages);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshMsg();
            }
        });
    }


    private void onParseIntent() {
        Intent parseIntent = getIntent();
        accountId = parseIntent.getStringExtra("accountId");

    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.actionbar_conver_iconback:
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }


    private void refreshMsg(){
        this.refreshMsg(true);
    }
    private void refreshMsg(boolean toLast) {
        msgList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        adapter.notifyDataSetChanged();
        if (toLast){
            msgList.setSelection(msgList.getBottom());
        }
    }

    @Override
    public void onRefresh() {
        MsgHelper.pullToRefresh(accountId,items.get(0),refreshCallback);
    }
}
