package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by AlienLeeH on 2016/7/12..Hour:06
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class PhotoViewActivity extends BaseActivity{
    private static final String EXTRA = "photoPath";
    private ImageView imageView;
    private IMMessage message;
    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            if (msg.isTheSame(message)){
                return;
            }
            if (isOriginImageHasDownloaded(message)){
                onDownLoadSuccess(msg);
            }else if (msg.getAttachStatus() == AttachStatusEnum.fail){
                onDownloadFailed();
            }
        }
    };

    private void onDownloadFailed() {
        ToastUtils.showToast(this,"图片下载失败");
    }

    @Override
    public void initView() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_photoview);
        imageView = (ImageView) findViewById(R.id.fullscreen_photoview);
        message = (IMMessage) getIntent().getSerializableExtra(EXTRA);
        registerObserver(true);
        if (isOriginImageHasDownloaded(message)){
            onDownLoadSuccess(message);
        }else {
            NIMClient.getService(MsgService.class).downloadAttachment(message,false);
        }

    }

    @Override
    protected void onDestroy() {
        registerObserver(false);
        super.onDestroy();
    }

    private void registerObserver(boolean b) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver,b);
    }

    private void onDownLoadSuccess(IMMessage message) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        String path = ((ImageAttachment)message.getAttachment()).getPath();
        ImageLoaderHelper.displayFile(path,imageView,options);

    }

    private boolean isOriginImageHasDownloaded(IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((ImageAttachment)message.getAttachment()).getPath())) {
            return true;
        }
        return false;
    }
    @Override
    public void initActionBar() {

    }

    @Override
    public void initListener() {
        imageView.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void processClick(View view) {
        if (view.getId() == R.id.fullscreen_photoview){
            finish();
        }

    }

    public static void start(Context context, IMMessage path) {
        Intent intent = new Intent(context,PhotoViewActivity.class);
        intent.putExtra(EXTRA,path);
        context.startActivity(intent);
    }
}
