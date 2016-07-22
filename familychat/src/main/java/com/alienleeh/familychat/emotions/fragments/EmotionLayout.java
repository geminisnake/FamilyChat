package com.alienleeh.familychat.emotions.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alienleeh.familychat.EmotionInputDetector;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.SimPagerAdapter;
import com.alienleeh.familychat.base.MyFragment;
import com.alienleeh.familychat.customUI.NoScrollViewPager;
import com.alienleeh.familychat.emotions.EmotionFragmentFactory;
import com.alienleeh.familychat.emotions.adapter.EmotionTabAdapter;
import com.alienleeh.familychat.emotions.GlobalOnItemClickManagerUtils;
import com.alienleeh.familychat.emotions.bean.EmotionTab;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class EmotionLayout extends MyFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NoScrollViewPager viewPager;
    private View mContentView;
    private EditText typemsg;
    private EmotionTabAdapter.OnItemClickListener listener;
    private EmotionTabAdapter tabAdapter;
    private EmotionInputDetector detector;
    private Button bt_sendmessage;
    private String accountId;
    Random random = new Random();
    private LinearLayout emotion_container;
    private Button bt_audio_msg;
    private Button bt_send_audio;
    private boolean touched;
    private AudioRecorder audioRecorder;
    private boolean started;
    private boolean canceled;
    private View audioView;
    private TextView timer_tip;
    private LinearLayout timer_tip_container;
    private Chronometer audio_timer;

    public void setSendMsgListener(SendMsgListener sendMsgListener) {
        this.sendMsgListener = sendMsgListener;
    }

    private SendMsgListener sendMsgListener;

    public void togglePanel() {
        if (emotion_container.isShown()){
            emotion_container.setVisibility(View.GONE);
        }
    }

    public void setAudioView(View audioView) {
        this.audioView = audioView;
        timer_tip = (TextView) audioView.findViewById(R.id.timer_tip);
        timer_tip_container = (LinearLayout) audioView.findViewById(R.id.timer_tip_container);
        audio_timer = (Chronometer) audioView.findViewById(R.id.audio_timer);
    }

    public interface SendMsgListener{

        void sendMsg(IMMessage message);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.accountId = args.getString("accountId");
        View rootView = inflater.inflate(R.layout.reply_layout,container,false);
        initView(rootView);
        initListener();
        initData();
        return rootView;
    }

    private void initListener() {
        listener = new EmotionTabAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewPager.setCurrentItem(position);
            }
        };
        bt_audio_msg.setOnClickListener(this);
        bt_sendmessage.setOnClickListener(this);
        bt_send_audio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        touched = true;
                        initAudioRecorder();
                        onStartAudioRecord();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        touched = false;
                        onEndRecorder(isCancel(view,motionEvent));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touched = false;
                        cancelAudioRecord(isCancel(view,motionEvent));
                        break;
                }
                return false;
            }
        });
        GlobalOnItemClickManagerUtils managerUtils = GlobalOnItemClickManagerUtils.getInstance(getContext());
        managerUtils.attachToEditText(typemsg);

    }
    /*
    --------------------------------------语音面板相关---------------------------------------------------------
     */

    /**
     *
     *
     */
    private void onStartAudioRecord() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        started = audioRecorder.startRecord();
        canceled = false;
        if (!started){
            ToastUtils.showToast(getContext(),"初始化录音失败");
            return;
        }
        if (!touched){
            return;
        }
        bt_send_audio.setText("松开 结束");
        updateTimerTip();
        playAudioRecordAnimate();
    }

    /**
     * 开始录音计时
     */
    private void playAudioRecordAnimate() {
        audioView.setVisibility(View.VISIBLE);
        audio_timer.setBase(SystemClock.elapsedRealtime());
        audio_timer.start();
    }

    /**
     * 结束语音录制
     *
     * @param cancel 是否是上滑取消的，已决定是否保存录音并发送
     */
    private void onEndRecorder(boolean cancel) {
        getActivity().getWindow().setFlags(0,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioRecorder.completeRecord(cancel);
        bt_send_audio.setText("按住 说话");
        stopAudioRecordAnimate();
    }
    /**
     *手指滑动切换
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        if (!started){
            return;
        }
        if (canceled == cancel){
            return;
        }
        canceled = cancel;
        updateTimerTip();
    }

    private void stopAudioRecordAnimate() {
        audio_timer.stop();
        audio_timer.setBase(SystemClock.elapsedRealtime());
        audioView.setVisibility(View.GONE);
    }

    private boolean isCancel(View view, MotionEvent motionEvent) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (motionEvent.getRawX() < location[0] ||
                motionEvent.getRawX() > location[0] + view.getWidth() ||
                motionEvent.getRawY() < location[1] - 40){
            return true;
        }else {
            return false;
        }
    }


    private IAudioRecordCallback recorderCallback = new IAudioRecordCallback() {
        @Override
        public void onRecordReady() {

        }

        @Override
        public void onRecordStart(File file, RecordType recordType) {

        }

        @Override
        public void onRecordSuccess(File file, long audioLength, RecordType recordType) {
            IMMessage message = MessageBuilder.createAudioMessage(accountId,SessionTypeEnum.P2P,file,audioLength);
            sendMsgListener.sendMsg(message);
        }

        @Override
        public void onRecordFail() {

        }

        @Override
        public void onRecordCancel() {

        }

        @Override
        public void onRecordReachedMaxTime(int time) {

        }
    };


    private void initAudioRecorder() {
        if (audioRecorder == null){
            audioRecorder = new AudioRecorder(getContext(), RecordType.AAC,AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND,recorderCallback);
        }

    }


    private void updateTimerTip() {
        if (canceled){
            timer_tip.setText("松开手指，结束语音");
            timer_tip_container.setBackgroundResource(R.drawable.bg_audio_cancel);
        }else {
            timer_tip.setText("手指上滑，取消发送");
            timer_tip_container.setBackgroundResource(0);
        }
    }

    /*
    --------------------------------------
     */
    private void initView(View rootView) {
        viewPager = (NoScrollViewPager) rootView.findViewById(R.id.viewpager_emoji_group);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tab_emoji_group);
        typemsg = (EditText) rootView.findViewById(R.id.et_conversation_typemessage);
        bt_sendmessage = (Button) rootView.findViewById(R.id.bt_conver_sendmessage);
        bt_audio_msg = (Button) rootView.findViewById(R.id.bt_audio_msg);
        bt_send_audio = (Button) rootView.findViewById(R.id.bt_send_audio);
        emotion_container = (LinearLayout) rootView.findViewById(R.id.emotion_container);
        detector = EmotionInputDetector.with(getActivity())
                .setEmotionView(emotion_container)
                .bindToContent(mContentView)
                .bindToEditText(typemsg)
                .bindToEmotionButton(rootView.findViewById(R.id.bt_show_emoji))
                .build();


    }
    private void initData() {
        List<EmotionTab> list = new ArrayList<>();
        list.add(new EmotionTab("1","sticker/icon_emotion/emoji_00.png"));
        list.add(new EmotionTab("2","sticker/icon_emotion/jj_normal.png"));
        list.add(new EmotionTab("3","sticker/icon_emotion/xxy_s_pressed.png"));
        list.add(new EmotionTab("4","sticker/icon_emotion/lt_s_pressed.png"));
        tabAdapter = new EmotionTabAdapter(getContext(),list);
        tabAdapter.setOnItemClickListener(listener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(tabAdapter);

        List<Fragment> fragments = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putString("sessionId",accountId);
        FunctionPage functionPage = FunctionPage.newInstance(FunctionPage.class,bundle);
        functionPage.setMsgSendListener(sendMsgListener);
        functionPage.setmEditText(typemsg);
        fragments.add(functionPage);

        fragments.add(EmotionFragmentFactory.getInstance().getFragment(1,true));

        EmotionCollectionFragment jjfragment = EmotionFragmentFactory.getInstance().getFragment(2,false,accountId);
        jjfragment.setSendMsgListener(sendMsgListener);
        fragments.add(jjfragment);

        EmotionCollectionFragment bearFragment = EmotionFragmentFactory.getInstance().getFragment(3,false,accountId);
        bearFragment.setSendMsgListener(sendMsgListener);
        fragments.add(bearFragment);

        EmotionCollectionFragment rabbitFragment = EmotionFragmentFactory.getInstance().getFragment(4,false,accountId);
        rabbitFragment.setSendMsgListener(sendMsgListener);
        fragments.add(rabbitFragment);

        final SimPagerAdapter groupAdapter = new SimPagerAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(groupAdapter);
        viewPager.setCurrentItem(1);

    }

    public void setContentView(View msgList) {
        this.mContentView = msgList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_conver_sendmessage){
            sendMessage();
        }else if (v.getId() == R.id.bt_audio_msg){
            togglePanel();
            toggleInputType();
        }

    }

    private void toggleInputType() {
        if (typemsg.isShown()){
            typemsg.setVisibility(View.GONE);
            bt_send_audio.setVisibility(View.VISIBLE);
            bt_audio_msg.setBackgroundResource(R.drawable.keyboard);
        }else {
            typemsg.setVisibility(View.VISIBLE);
            bt_send_audio.setVisibility(View.GONE);
            bt_audio_msg.setBackgroundResource(R.drawable.bt_audio_msg);
        }
    }

    private void sendMessage() {
        if (!TextUtils.isEmpty(typemsg.getText())){
            IMMessage imMessage = MessageBuilder.createTextMessage(
                    accountId,
                    SessionTypeEnum.P2P,
                    typemsg.getText().toString());
            sendMsgListener.sendMsg(imMessage);
            typemsg.setText("");
        }
    }

}
