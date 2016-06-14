package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.emotions.util.SpanStringUtils;
import com.alienleeh.familychat.helper.AudioPlayHandler;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.helper.PopupWindowHelper;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.ui.activity.ConversationActivity;
import com.alienleeh.familychat.ui.activity.MapPickActivity;
import com.alienleeh.familychat.ui.activity.PhotoViewActivity;
import com.alienleeh.familychat.ui.activity.UserInfoActiviy;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;
import com.alienleeh.familychat.utils.MyBitmapDecoder;
import com.alienleeh.familychat.utils.TimesUtil;
import com.alienleeh.familychat.utils.ToastUtils;
import com.alienleeh.familychat.utils.file.FileUtil;
import com.alienleeh.mylibrary.customUI.CircleImageView;
import com.alienleeh.mylibrary.customUI.ProgressImageView;
import com.jauker.widget.BadgeView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/30.<br/>
 *
 * description:消息会话界面由ListView实现，这是其适配器
 */
public class ConversationDetailAdapter extends BaseAdapter {

    private static final long FIVE_MIN = 1000 * 60 * 5;
    private static final String LOCATION_IMAGE = "file_picker/nim_location_bk.png";
    String sesId;
    List<IMMessage> messages;
    LayoutInflater inflater;
    Context context;
    private final DisplayImageOptions options;

    HashMap<String,View> viewHashMap;
    private List<PopupWindow> popupItems = new ArrayList<>();
    private List<IMMessage> showTimeMsgs = new ArrayList<>();
    private Handler handler;
    private AudioPlayer audioPlayer;

    public ConversationDetailAdapter(Context context,List<IMMessage> imMessages, @NonNull String accountId) {
        messages = imMessages;
        initializeTime();
        sesId = accountId;
        this.context = context;
        inflater = LayoutInflater.from(context);
        viewHashMap = new HashMap<>();
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(false)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    private void initializeTime() {
        showTimeMsgs.clear();
        if (messages == null || messages.isEmpty()){
            return;
        }
        for (int i = 0;i < messages.size();i++){
            if (i == 0){
                showTimeMsgs.add(messages.get(i));
            }else {
                long time = messages.get(i).getTime()-showTimeMsgs.get(showTimeMsgs.size()-1).getTime();
                if (time >= FIVE_MIN){
                    showTimeMsgs.add(messages.get(i));
                }
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        initializeTime();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (messages == null){
            return 0;
        }
        return messages.size();
    }

    @Override
    public IMMessage getItem(int position) {
        if (messages == null){
            return null;
        }
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_conversation_detail,parent,false);
        }
        MsgHolder holder = getHolder(convertView);
        IMMessage message = getItem(position);
        if (showTimeMsgs.contains(message)){
            holder.tv_time.setVisibility(View.VISIBLE);
            showTime(holder,message.getTime());
        }else {
            holder.tv_time.setVisibility(View.GONE);
        }
        if (message.getDirect()== MsgDirectionEnum.In){
            holder.sendView.setVisibility(View.GONE);
            holder.receiveView.setVisibility(View.VISIBLE);
            inflateContent(message,holder.containerRec);
        }else {
            holder.receiveView.setVisibility(View.GONE);
            holder.sendView.setVisibility(View.VISIBLE);
            inflateContent(message,holder.containerSend);
        }
        return convertView;
    }

    private void inflateContent(final IMMessage message, final FrameLayout contentView) {
        contentView.removeAllViews();
        MsgStatusEnum msgStaus = message.getStatus();
        AttachStatusEnum attachStatus = message.getAttachStatus();

        View view = null;
        switch (message.getMsgType()) {
            case text:
                view = View.inflate(context,R.layout.msgtextview,contentView);
                TextView textView = (TextView)view.findViewById(R.id.textview_msg);
                SpannableString string = SpanStringUtils.getEmotionContent(true,context, textView, message.getContent());
                textView.setText(string);
                if (contentView.getId() == R.id.tv_msg_send){
                    textView.setTextColor(Color.WHITE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case image:
                view = View.inflate(context,R.layout.msgimageview,contentView);
                final ProgressImageView imageView = (ProgressImageView) view.findViewById(R.id.thumbimage_view);
                ImageAttachment attachment = (ImageAttachment) message.getAttachment();
                String Thumbpath = attachment.getThumbPath();
                final String path = attachment.getPath();
                viewHashMap.put(message.getUuid(),imageView);
                setImageSize(path,attachment,imageView);
                if (Thumbpath != null){
                    imageView.loadAsPath(Thumbpath,options);
                }else if (path != null){
                    imageView.loadAsPath(path,options);
                }else {
                    imageView.loadAsResource(R.drawable.default_img);
                    if (message.getAttachStatus() == AttachStatusEnum.transferred || message.getAttachStatus() == AttachStatusEnum.def){
                        downloadAttach(message,true);
                    }
                }
                if (message.getAttachStatus() == AttachStatusEnum.transferred|| message.getAttachStatus() == AttachStatusEnum.def){
                    imageView.setProgress(100);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.e(this,"click success");
                        PhotoViewActivity.start(context,message);
                    }
                });
                break;
            case audio:
                view = View.inflate(context,R.layout.msgaudioview,contentView);
                final AudioAttachment audioAttachment = (AudioAttachment)message.getAttachment();
                final ImageView l_audio_anim = (ImageView) view.findViewById(R.id.audio_icon_anim_left);
                final ImageView r_audio_anim = (ImageView) view.findViewById(R.id.audio_icon_anim_right);
                final TextView audiomsg = (TextView) view.findViewById(R.id.textaudio_msg);
                final BadgeView badgeView = new BadgeView(context);
                //红点
                if (isReceived(message)){
                    r_audio_anim.setVisibility(View.GONE);
                    if (attachStatus == AttachStatusEnum.transferred && msgStaus == MsgStatusEnum.read){
                        badgeView.setTargetView(view);
                        badgeView.setBadgeCount(1);
                        badgeView.setGravity(Gravity.RIGHT);
                    }
                }else {
                    l_audio_anim.setVisibility(View.GONE);
                }
                updatePlayTime(audiomsg,audioAttachment.getDuration());
                final AudioPlayHandler playHandler = new AudioPlayHandler(context,message, new OnPlayListener() {
                    @Override
                    public void onPrepared() {
                        playAnim(isReceived(message)? l_audio_anim : r_audio_anim);
                    }

                    @Override
                    public void onCompletion() {
                        stopAnim(isReceived(message)? l_audio_anim : r_audio_anim);
                    }

                    @Override
                    public void onInterrupt() {
                        stopAnim(isReceived(message)? l_audio_anim : r_audio_anim);
                    }

                    @Override
                    public void onError(String s) {
                        ToastUtils.showToast(context,s);
                        stopAnim(isReceived(message)? l_audio_anim : r_audio_anim);
                    }

                    @Override
                    public void onPlaying(long l) {
                        updatePlayTime(audiomsg,l);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (message.getDirect() == MsgDirectionEnum.In && message.getAttachStatus() != AttachStatusEnum.transferred){
                            return;
                        }
                        badgeView.setBadgeCount(0);
                        playHandler.handleClick();
                    }
                });

                break;
            case location:
                view = View.inflate(context,R.layout.msglocationview,contentView);
                ProgressImageView locationImage = (ProgressImageView) view.findViewById(R.id.message_item_location_image);
                locationImage.setProgress(100);
                //位置消息没有进度，手动设置100关闭
                TextView locationText = (TextView) view.findViewById(R.id.message_item_location_address);
                LocationAttachment loAttach = (LocationAttachment) message.getAttachment();
                locationText.setText(loAttach.getAddress());
                setLocationSize(locationImage,locationText);
                locationImage.loadAsAsset(LOCATION_IMAGE,options);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MapPickActivity.start(context,message);
                    }
                });
                break;

            case file:
                view = View.inflate(context, R.layout.msgfileview,contentView);
                TextView fileName = (TextView) view.findViewById(R.id.message_item_file_name_label);
                TextView fileStatus = (TextView) view.findViewById(R.id.message_item_file_status_label);
                ProgressBar filePro = (ProgressBar) view.findViewById(R.id.message_item_file_transfer_progress_bar);
                FileAttachment fileAttachment = (FileAttachment) message.getAttachment();
                String filepath = fileAttachment.getPath();
                fileName.setText(fileAttachment.getDisplayName());
                viewHashMap.put(message.getUuid(),filePro);

                if (!TextUtils.isEmpty(filepath)){
                    fileStatus.setVisibility(View.VISIBLE);
                    String fileSize = FileUtil.formatFileSize(fileAttachment.getSize());
                    fileStatus.setText(fileSize);
                    filePro.setVisibility(View.GONE);
                }else {
                    switch (message.getAttachStatus()){
                        case transferring:
                            filePro.setVisibility(View.VISIBLE);
                            fileStatus.setVisibility(View.GONE);
                            break;
                        case def:
                        case transferred:
                        case fail:
                            fileStatus.setVisibility(View.VISIBLE);
                            filePro.setVisibility(View.GONE);
                            StringBuilder sb = new StringBuilder();
                            sb.append(FileUtil.formatFileSize(fileAttachment.getSize()));
                            sb.append("  ");
                            sb.append(FileUtil.isFileExist(fileAttachment.getPathForSave())? "已下载":"未下载");
                            fileStatus.setText(sb.toString());
                            break;
                    }
                }
                break;
        }
        if (view != null)
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final PopupWindow window = PopupWindowHelper.getMsgHandlePopup(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, new PopupWindowHelper.MsgHandleListener() {
                    @Override
                    public void onForward() {
                        handleMsg(ConversationActivity.MSG_FORWARD);
                    }
                    void handleMsg(int flag){
                        Message imsg = new Message();
                        imsg.what = flag;
                        imsg.obj = message;
                        handler.sendMessage(imsg);
                    }
                    @Override
                    public void onDelete() {
                        handleMsg(ConversationActivity.MSG_DELETE_ITEM);
                    }
                });
                popupItems.add(window);
                window.showAsDropDown(view);

                return true;
            }
        });
    }

    private void stopAnim(ImageView imageView) {
        if (imageView.getBackground() instanceof AnimationDrawable){
            AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
            anim.stop();
            anim.selectDrawable(2);
        }
    }

    private void playAnim(ImageView imageView) {
        if (imageView.getBackground() instanceof AnimationDrawable){
            AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
            anim.start();
        }
    }

    /**
     * 把毫秒转化 并显示到控件上<br/>
     * @param view 要显示的控件
     * @param duration  显示时间
     */
    private void updatePlayTime(TextView view, long duration) {
        long durationSeconds = TimesUtil.getSecondsByMillSec(duration);
        view.setText(durationSeconds >= 0 ? durationSeconds + "\"" : "");
    }

    private boolean isReceived(IMMessage message) {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    private void setLocationSize(ProgressImageView locationImage, TextView locationText) {
        ViewGroup.LayoutParams params = locationImage.getLayoutParams();
        try {
            InputStream stream = context.getAssets().open(LOCATION_IMAGE);
            int[] bounds = MyBitmapDecoder.decodeBounds(stream);
            int[] rightBounds = MyBitmapDecoder.getThumbnailDisplaySize(bounds[0],bounds[1],getImageMaxEdge(),getImageMinEdge());
            params.width = rightBounds[0];
            params.height = rightBounds[1];
            locationImage.setLayoutParams(params);
            locationText.setMaxWidth(bounds[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setImageSize(String path, ImageAttachment attachment, ProgressImageView imageView) {
        int[] bounds1 = null;
        if (path != null){
            bounds1 = MyBitmapDecoder.decodeBounds(path);
        }
        if (bounds1 == null){
            bounds1 = new int[]{attachment.getWidth(),attachment.getHeight()};
        }

        int[] bounds2 = MyBitmapDecoder.getThumbnailDisplaySize(bounds1[0], bounds1[1], getImageMaxEdge(), getImageMinEdge());
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = bounds2[0];
        params.height = bounds2[1];
        imageView.setLayoutParams(params);
    }


    private void downloadAttach(IMMessage message, boolean thumb) {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment){
            NIMClient.getService(MsgService.class).downloadAttachment(message,thumb);
        }
    }

    private static int getImageMaxEdge() {
        return ApplicationUtils.getWidth() / 2;
    }
    private static int getImageMinEdge() {
        return ApplicationUtils.getWidth() / 4;
    }


    private void showTime(MsgHolder holder, long time) {
        if (DateUtils.isToday(time)){
            holder.tv_time.setText(DateFormat.getTimeFormat(context).format(time));
        }else {
            holder.tv_time.setText(DateFormat.getDateFormat(context).format(time));
        }
    }

    private MsgHolder getHolder(View convertView) {
        MsgHolder holder = (MsgHolder) convertView.getTag();
        if (holder == null){
            holder = new MsgHolder(convertView);
            convertView.setTag(holder);
        }
        return holder;
    }

    public void notifyProgressChanged(String uuid, int progress, MsgTypeEnum type) {

        View view = viewHashMap.get(uuid);
        if (view!=null){
            switch (type){
                case file:
                    ((ProgressBar)view).setProgress(progress);
                    break;
                case image:
                    ((ProgressImageView)view).setProgress(progress);
                    break;
            }
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public boolean clearPopup() {
        if (!popupItems.isEmpty()) {
            for (PopupWindow window : popupItems) {
                window.dismiss();
            }
            popupItems.clear();
            return true;
        }
        return false;
    }


    class MsgHolder{
        TextView tv_time;
        LinearLayout receiveView;
        RelativeLayout sendView;
        FrameLayout containerRec;
        FrameLayout containerSend;
        public MsgHolder(View v) {
            tv_time = (TextView) v.findViewById(R.id.tv_sms_time);
            receiveView = (LinearLayout) v.findViewById(R.id.check_dismiss_conver1);
            sendView = (RelativeLayout) v.findViewById(R.id.check_dismiss_conver2);
            containerRec = (FrameLayout) v.findViewById(R.id.tv_msg_received);
            containerSend = (FrameLayout) v.findViewById(R.id.tv_msg_send);
            setImage(v);

        }
    }

    private void setImage(View v) {
        CircleImageView himAvatar = (CircleImageView) v.findViewById(R.id.himavatar_conversation);
        CircleImageView selfAvatar = (CircleImageView) v.findViewById(R.id.selfavatar_conversation);
        String myPath = UserInfoCache.getInstance().getAvatar(NIMClientManager.getAccount());
        if (!TextUtils.isEmpty(myPath)){
            ImageLoaderHelper.displayAvatarList(myPath,selfAvatar);
        }
        String sesPath = UserInfoCache.getInstance().getAvatar(sesId);
        if (!TextUtils.isEmpty(sesPath)){
            ImageLoaderHelper.displayAvatarList(sesPath,himAvatar);
        }else {
            ImageLoaderHelper.displayAvatarList("defaultavatar.png",himAvatar);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.himavatar_conversation:
                        UserInfoActiviy.start(context,sesId);
                        break;
                    case R.id.selfavatar_conversation:
                        UserInfoActiviy.start(context,NIMClientManager.getAccount());
                        break;
                }

            }
        };
        selfAvatar.setOnClickListener(listener);
        himAvatar.setOnClickListener(listener);
    }
}
