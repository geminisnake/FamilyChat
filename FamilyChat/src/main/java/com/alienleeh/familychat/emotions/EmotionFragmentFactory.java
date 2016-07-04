package com.alienleeh.familychat.emotions;

import android.os.Bundle;

import com.alienleeh.familychat.emotions.fragments.EmotionCollectionFragment;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class EmotionFragmentFactory {
    public static final String EMOTION_TYPE = "emotion_type";
    public static final String FONT_SCALE = "BigOrSmall";
    public static final String ACCOUNT_ID = "accountId";
    private static EmotionFragmentFactory instance = null;

    private EmotionFragmentFactory() {
    }

    public static synchronized EmotionFragmentFactory getInstance() {
        if (instance == null) {
            instance = new EmotionFragmentFactory();
        }
        return instance;
    }
    public EmotionCollectionFragment getFragment(int emotionType,boolean scale){
        Bundle bundle = new Bundle();
        bundle.putInt(EMOTION_TYPE,emotionType);
        bundle.putBoolean(FONT_SCALE,scale);
        return EmotionCollectionFragment.newInstance(EmotionCollectionFragment.class,bundle);
    }
    /*

     */
    public EmotionCollectionFragment getFragment(int emotionType, boolean scale, String accountId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EMOTION_TYPE,emotionType);
        bundle.putBoolean(FONT_SCALE,scale);
        bundle.putString(ACCOUNT_ID,accountId);
        return EmotionCollectionFragment.newInstance(EmotionCollectionFragment.class,bundle);
    }
}
