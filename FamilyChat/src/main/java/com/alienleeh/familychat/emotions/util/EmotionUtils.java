package com.alienleeh.familychat.emotions.util;


import android.support.v4.util.ArrayMap;

import java.util.HashSet;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class EmotionUtils {

    public enum EMOTION_TYPE{
        CLASSIC_EMOTION,
        JJ_EMOTION,
        BEAR_EMOTION,
        RABBIT_EMOTION,
        UNKNOWN,
        fetchALL_EMOTION,
        EMOTION_TYPE() {

        };

        public static EMOTION_TYPE valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return CLASSIC_EMOTION;
            case 2:
                return JJ_EMOTION;
            case 3:
                return BEAR_EMOTION;
            case 4:
                return RABBIT_EMOTION;
            case 10:
                return fetchALL_EMOTION;
            default:
                return UNKNOWN;
        }
    }
}
    public static String getImgByName(int type, String key) {
        switch (EMOTION_TYPE.valueOf(type)){
            case CLASSIC_EMOTION:
                return "sticker/classic_emoji/" + EMOTION_CLASSIC_MAP.get(key);
            case JJ_EMOTION:
                return "sticker/ajmd/" + EMOTION_AJMD.get(key);
            case BEAR_EMOTION:
                return "sticker/xxy/" + EMOTION_BEAR.get(key);
            case RABBIT_EMOTION:
                return "sticker/lt/" + EMOTION_RABBIT.get(key);
            default:
                return "";
        }
    }
    public static HashSet<Integer> ALL_EMOTION;

    public static ArrayMap<String, String> EMPTY_MAP;
    public static ArrayMap<String, String> EMOTION_CLASSIC_MAP;
    public static ArrayMap<String, String> EMOTION_AJMD;
    public static ArrayMap<String, String> EMOTION_BEAR;
    public static ArrayMap<String, String> EMOTION_RABBIT;
    static {
        ALL_EMOTION = new HashSet<>();
        ALL_EMOTION.add(1);
        ALL_EMOTION.add(2);
        ALL_EMOTION.add(3);
        ALL_EMOTION.add(4);

        EMPTY_MAP = new ArrayMap<>();
        EMOTION_AJMD = new ArrayMap<>();
        EMOTION_BEAR = new ArrayMap<>();
        EMOTION_RABBIT = new ArrayMap<>();

        EMOTION_CLASSIC_MAP = new ArrayMap<>();
        EMOTION_CLASSIC_MAP.put("[大笑]", "emoji_00.png");
        EMOTION_CLASSIC_MAP.put("[可爱]", "emoji_01.png");
        EMOTION_CLASSIC_MAP.put("[色]", "emoji_02.png");
        EMOTION_CLASSIC_MAP.put("[嘘]", "emoji_03.png");
        EMOTION_CLASSIC_MAP.put("[亲]", "emoji_04.png");
        EMOTION_CLASSIC_MAP.put("[呆]", "emoji_05.png");
        EMOTION_CLASSIC_MAP.put("[口水]", "emoji_06.png");
        EMOTION_CLASSIC_MAP.put("[呲牙]", "emoji_07.png");
        EMOTION_CLASSIC_MAP.put("[鬼脸]", "emoji_08.png");
        EMOTION_CLASSIC_MAP.put("[害羞]", "emoji_09.png");
        EMOTION_CLASSIC_MAP.put("[偷笑]", "emoji_10.png");
        EMOTION_CLASSIC_MAP.put("[调皮]", "emoji_11.png");
        EMOTION_CLASSIC_MAP.put("[可怜]", "emoji_12.png");
        EMOTION_CLASSIC_MAP.put("[敲]", "emoji_13.png");
        EMOTION_CLASSIC_MAP.put("[惊讶]", "emoji_14.png");
        EMOTION_CLASSIC_MAP.put("[流感]", "emoji_15.png");
        EMOTION_CLASSIC_MAP.put("[委屈]", "emoji_16.png");

        EMOTION_CLASSIC_MAP.put("[流泪]", "emoji_17.png");
        EMOTION_CLASSIC_MAP.put("[嚎哭]", "emoji_18.png");
        EMOTION_CLASSIC_MAP.put("[惊恐]", "emoji_19.png");
        EMOTION_CLASSIC_MAP.put("[怒]", "emoji_20.png");
        EMOTION_CLASSIC_MAP.put("[酷]", "emoji_21.png");
        EMOTION_CLASSIC_MAP.put("[不说]", "emoji_22.png");
        EMOTION_CLASSIC_MAP.put("[鄙视]", "emoji_23.png");
        EMOTION_CLASSIC_MAP.put("[阿弥陀佛]", "emoji_24.png");
        EMOTION_CLASSIC_MAP.put("[奸笑]", "emoji_25.png");

        EMOTION_CLASSIC_MAP.put("[睡着]", "emoji_26.png");
        EMOTION_CLASSIC_MAP.put("[口罩]", "emoji_27.png");
        EMOTION_CLASSIC_MAP.put("[努力]", "emoji_28.png");
        EMOTION_CLASSIC_MAP.put("[抠鼻孔]", "emoji_29.png");
        EMOTION_CLASSIC_MAP.put("[疑问]", "emoji_30.png");
        EMOTION_CLASSIC_MAP.put("[怒骂]", "emoji_31.png");
        EMOTION_CLASSIC_MAP.put("[晕]", "emoji_32.png");
        EMOTION_CLASSIC_MAP.put("[呕吐]", "emoji_33.png");
        EMOTION_CLASSIC_MAP.put("[拜一拜]", "emoji_160.png");
        EMOTION_CLASSIC_MAP.put("[惊喜]", "emoji_161.png");
        EMOTION_CLASSIC_MAP.put("[流汗]", "emoji_162.png");
        EMOTION_CLASSIC_MAP.put("[卖萌]", "emoji_163.png");
        EMOTION_CLASSIC_MAP.put("[默契眨眼]", "emoji_164.png");
        EMOTION_CLASSIC_MAP.put("[烧香拜佛]", "emoji_165.png");
        EMOTION_CLASSIC_MAP.put("[晚安]", "emoji_166.png");
        EMOTION_CLASSIC_MAP.put("[强]", "emoji_34.png");

        EMOTION_AJMD.put("[鸡一]","ajmd001.png");
        EMOTION_AJMD.put("[鸡二]","ajmd002.png");
        EMOTION_AJMD.put("[鸡三]","ajmd003.png");
        EMOTION_AJMD.put("[鸡四]","ajmd004.png");
        EMOTION_AJMD.put("[鸡五]","ajmd005.png");
        EMOTION_AJMD.put("[鸡六]","ajmd006.png");
        EMOTION_AJMD.put("[鸡七]","ajmd007.png");
        EMOTION_AJMD.put("[鸡八]","ajmd008.png");
        EMOTION_AJMD.put("[鸡九]","ajmd009.png");
        EMOTION_AJMD.put("[鸡十]","ajmd010.png");
        EMOTION_AJMD.put("[鸡十一]","ajmd011.png");
        EMOTION_AJMD.put("[鸡十二]","ajmd012.png");
        EMOTION_AJMD.put("[鸡十三]","ajmd013.png");
        EMOTION_AJMD.put("[鸡十四]","ajmd014.png");
        EMOTION_AJMD.put("[鸡十五]","ajmd015.png");
        EMOTION_AJMD.put("[鸡十六]","ajmd016.png");
        EMOTION_AJMD.put("[鸡十七]","ajmd017.png");
        EMOTION_AJMD.put("[鸡十八]","ajmd018.png");
        EMOTION_AJMD.put("[鸡十九]","ajmd019.png");
        EMOTION_AJMD.put("[鸡二十]","ajmd020.png");
        EMOTION_AJMD.put("[鸡二一]","ajmd021.png");
        EMOTION_AJMD.put("[鸡二十二]","ajmd022.png");
        EMOTION_AJMD.put("[鸡二十三]","ajmd023.png");
        EMOTION_AJMD.put("[鸡二十四]","ajmd024.png");
        EMOTION_AJMD.put("[鸡二十五]","ajmd025.png");
        EMOTION_AJMD.put("[鸡二十六]","ajmd026.png");
        EMOTION_AJMD.put("[鸡二十七]","ajmd027.png");
        EMOTION_AJMD.put("[鸡二十八]","ajmd028.png");
        EMOTION_AJMD.put("[鸡二十九]","ajmd029.png");
        EMOTION_AJMD.put("[鸡三十]","ajmd030.png");
        EMOTION_AJMD.put("[鸡三一]","ajmd031.png");
        EMOTION_AJMD.put("[鸡三二]","ajmd032.png");
        EMOTION_AJMD.put("[鸡三三]","ajmd033.png");
        EMOTION_AJMD.put("[鸡三四]","ajmd034.png");
        EMOTION_AJMD.put("[鸡三五]","ajmd035.png");
        EMOTION_AJMD.put("[鸡三六]","ajmd036.png");
        EMOTION_AJMD.put("[鸡三七]","ajmd037.png");
        EMOTION_AJMD.put("[鸡三八]","ajmd038.png");
        EMOTION_AJMD.put("[鸡三九]","ajmd039.png");
        EMOTION_AJMD.put("[鸡四十]","ajmd040.png");
        EMOTION_AJMD.put("[鸡四一]","ajmd041.png");
        EMOTION_AJMD.put("[鸡四二]","ajmd042.png");
        EMOTION_AJMD.put("[鸡四三]","ajmd043.png");
        EMOTION_AJMD.put("[鸡四四]","ajmd044.png");
        EMOTION_AJMD.put("[鸡四五]","ajmd045.png");
        EMOTION_AJMD.put("[鸡四六]","ajmd046.png");
        EMOTION_AJMD.put("[鸡四七]","ajmd047.png");
        EMOTION_AJMD.put("[鸡四八]","ajmd048.png");

        EMOTION_BEAR.put("[熊一]","xxy001.png");
        EMOTION_BEAR.put("[熊二]","xxy002.png");
        EMOTION_BEAR.put("[熊三]","xxy003.png");
        EMOTION_BEAR.put("[熊四]","xxy004.png");
        EMOTION_BEAR.put("[熊五]","xxy005.png");
        EMOTION_BEAR.put("[熊六]","xxy006.png");
        EMOTION_BEAR.put("[熊七]","xxy007.png");
        EMOTION_BEAR.put("[熊八]","xxy008.png");
        EMOTION_BEAR.put("[熊九]","xxy009.png");
        EMOTION_BEAR.put("[熊十]","xxy010.png");
        EMOTION_BEAR.put("[熊十一]","xxy011.png");
        EMOTION_BEAR.put("[熊十二]","xxy012.png");
        EMOTION_BEAR.put("[熊十三]","xxy013.png");
        EMOTION_BEAR.put("[熊十四]","xxy014.png");
        EMOTION_BEAR.put("[熊十五]","xxy015.png");
        EMOTION_BEAR.put("[熊十六]","xxy016.png");
        EMOTION_BEAR.put("[熊十七]","xxy017.png");
        EMOTION_BEAR.put("[熊十八]","xxy018.png");
        EMOTION_BEAR.put("[熊十九]","xxy019.png");
        EMOTION_BEAR.put("[熊二十]","xxy020.png");
        EMOTION_BEAR.put("[熊二一]","xxy021.png");
        EMOTION_BEAR.put("[熊二二]","xxy022.png");
        EMOTION_BEAR.put("[熊二三]","xxy023.png");
        EMOTION_BEAR.put("[熊二四]","xxy024.png");
        EMOTION_BEAR.put("[熊二五]","xxy025.png");
        EMOTION_BEAR.put("[熊二六]","xxy026.png");
        EMOTION_BEAR.put("[熊二七]","xxy027.png");
        EMOTION_BEAR.put("[熊二八]","xxy028.png");
        EMOTION_BEAR.put("[熊二九]","xxy029.png");
        EMOTION_BEAR.put("[熊三十]","xxy030.png");
        EMOTION_BEAR.put("[熊三十一]","xxy031.png");
        EMOTION_BEAR.put("[熊三十二]","xxy032.png");
        EMOTION_BEAR.put("[熊三十三]","xxy033.png");
        EMOTION_BEAR.put("[熊三十四]","xxy034.png");
        EMOTION_BEAR.put("[熊三十五]","xxy035.png");
        EMOTION_BEAR.put("[熊三十六]","xxy036.png");
        EMOTION_BEAR.put("[熊三十七]","xxy037.png");
        EMOTION_BEAR.put("[熊三十八]","xxy038.png");
        EMOTION_BEAR.put("[熊三十九]","xxy039.png");
        EMOTION_BEAR.put("[熊四十]","xxy040.png");

        EMOTION_RABBIT.put("[兔一]","lt001.png");
        EMOTION_RABBIT.put("[兔二]","lt002.png");
        EMOTION_RABBIT.put("[兔三]","lt003.png");
        EMOTION_RABBIT.put("[兔上]","lt004.png");
        EMOTION_RABBIT.put("[兔五]","lt005.png");
        EMOTION_RABBIT.put("[兔六]","lt006.png");
        EMOTION_RABBIT.put("[兔七]","lt007.png");
        EMOTION_RABBIT.put("[兔八]","lt008.png");
        EMOTION_RABBIT.put("[兔九]","lt009.png");
        EMOTION_RABBIT.put("[兔十]","lt010.png");
        EMOTION_RABBIT.put("[兔十一]","lt011.png");
        EMOTION_RABBIT.put("[兔十二]","lt012.png");
        EMOTION_RABBIT.put("[兔十三]","lt013.png");
        EMOTION_RABBIT.put("[兔十四]","lt014.png");
        EMOTION_RABBIT.put("[兔十五]","lt015.png");
        EMOTION_RABBIT.put("[兔十六]","lt016.png");
        EMOTION_RABBIT.put("[兔十七]","lt017.png");
        EMOTION_RABBIT.put("[兔十八]","lt018.png");
        EMOTION_RABBIT.put("[兔十九]","lt019.png");
        EMOTION_RABBIT.put("[兔二十]","lt020.png");
    }

    public static ArrayMap<String, String> FUCTIONS;
    static {
        FUCTIONS = new ArrayMap<>(1);
        FUCTIONS.put("图 片","function/photo_msg.png");
        FUCTIONS.put("位 置","function/location_msg.png");
        FUCTIONS.put("语音通话","function/voice_msg.png");
        FUCTIONS.put("视频通话","function/video_chat.png");
        FUCTIONS.put("文 件","function/file_msg.png");
    }

    public static ArrayMap<String,String> getEmotionMap(int type) {
        ArrayMap EMO = null;
        switch (EMOTION_TYPE.valueOf(type)){
            case CLASSIC_EMOTION:
                EMO = EMOTION_CLASSIC_MAP;
                break;
            case JJ_EMOTION:
                EMO = EMOTION_AJMD;
                break;
            case BEAR_EMOTION:
                EMO = EMOTION_BEAR;
                break;
            case RABBIT_EMOTION:
                EMO = EMOTION_RABBIT;
                break;
            default:
                EMO = EMPTY_MAP;
                break;
        }
        return EMO;
    }

}
