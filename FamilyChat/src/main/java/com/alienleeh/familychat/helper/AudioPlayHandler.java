package com.alienleeh.familychat.helper;

import android.content.Context;
import android.media.AudioManager;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by AlienLeeH on 2016/7/22..Hour:04
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class AudioPlayHandler{
    private OnPlayListener newListener;

    private AudioPlayer player;
    public AudioPlayHandler(Context context, IMMessage message, OnPlayListener onPlayListener) {
        AudioAttachment attachment = (AudioAttachment) message.getAttachment();
        player = new AudioPlayer(context,attachment.getPath(),onPlayListener);
    }


    public void handleClick() {
        if (player.isPlaying()){
            player.stop();
        }else {
            player.start(AudioManager.STREAM_MUSIC);
        }
    }
}
