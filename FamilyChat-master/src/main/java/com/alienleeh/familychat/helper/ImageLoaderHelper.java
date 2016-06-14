package com.alienleeh.familychat.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.utils.LogUtils;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by AlienLeeH on 2016/7/3.
 */
public class ImageLoaderHelper {

    private static final int M = 1024 * 1024;
    private Context mContext;
    private static final String TAG = "ImageLoaderHelper";
    private String path;


    public ImageLoaderHelper(Context context, ImageLoaderConfiguration configuration){
        this.mContext = context;
        init(configuration);
    }

    private void init(ImageLoaderConfiguration config) {
        try {
            ImageLoader.getInstance().init(config == null ? getDefaultConfig() : config);
        } catch (IOException e) {
            LogUtils.e(TAG,e.getMessage().toString());
        }
    }

    public void clear(){
        ImageLoader.getInstance().clearMemoryCache();
    }





    private ImageLoaderConfiguration getDefaultConfig() throws IOException {
        int MAX_CACHE_MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
        File cacheDir = StorageUtils.getOwnCacheDirectory(mContext, mContext.getPackageName() + "/cache/image/");

        LogUtils.i(this, "ImageLoader memory cache size = " + MAX_CACHE_MEMORY_SIZE / M + "M");
        LogUtils.i(this, "ImageLoader disk cache directory = " + cacheDir.getAbsolutePath());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(mContext)
                .threadPoolSize(3) // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级，减小对UI主线程的影响
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAX_CACHE_MEMORY_SIZE))
                .discCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 0))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 30 * 1000))
                // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()
                .build();

        return config;
    }

    public static void displayAvatar(NimUserInfo myInfo, ImageView im) {
        String path = ImageDownloader.Scheme.ASSETS.wrap(String.format("avatars/%s", myInfo.getAvatar()));

        ImageLoader.getInstance().displayImage(path,im,createAvatarOptions());
    }
    private static DisplayImageOptions createAvatarOptions(){
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.dddddddddefault)
                .cacheInMemory(true)
                .build();
    }
    private static DisplayImageOptions createImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static void displayAvatarList(String apath, ImageView im) {
        ImageLoader.getInstance().displayImage(getAvatarPath(apath),im,createAvatarOptions());
    }

    private static String getAvatarPath(String apath) {
        if (apath.equals("defaultavatar.png")){
            return ImageDownloader.Scheme.ASSETS.wrap(apath);
        }else {
            return ImageDownloader.Scheme.ASSETS.wrap(String.format("avatars/%s", apath));
        }
    }

    public static void displayResource(String rpath, ImageView imageView) {
    }

    public static void displayAsset(ImageView btn, String iconPath) {
        String apath;
        if (TextUtils.isEmpty(iconPath)){
            apath = ImageDownloader.Scheme.ASSETS.wrap("sticker/emotion_add.png");
        }else {
            apath = ImageDownloader.Scheme.ASSETS.wrap(iconPath);
        }
        ImageLoader.getInstance().displayImage(apath,btn);
    }

    public static void displayFile(String path, ImageView imageView, DisplayImageOptions options) {
        String uri = ImageDownloader.Scheme.FILE.wrap(path);
        ImageLoader.getInstance().displayImage(uri,imageView,options);
    }

    public static void displayAssetDef(String path, ImageView iv) {
        String apath = ImageDownloader.Scheme.ASSETS.wrap(path);
        ImageLoader.getInstance().displayImage(apath,iv);
    }
}
