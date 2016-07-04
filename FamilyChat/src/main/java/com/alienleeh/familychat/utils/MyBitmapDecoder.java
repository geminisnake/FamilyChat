package com.alienleeh.familychat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AlienLeeH on 2016/7/11..Hour:12
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class MyBitmapDecoder {
    public static int[] decodeBounds(String thumbPath) {
        InputStream is = null;
        try {
            is = new FileInputStream(thumbPath);
            int[] ints = decodeBounds(is);
            return ints;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new int[]{0,0};
    }

    public static int[] decodeBounds(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,options);
        return new int[]{options.outWidth,options.outHeight};
    }

    public static Bitmap decodeSample(String path, int width, int height) {
        return decodeSample(path,getSampleSize(path,width,height));
    }

    private static Bitmap decodeSample(String path, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return checkInBitMap(bitmap,options,path);
    }

    private static Bitmap checkInBitMap(Bitmap bitmap, BitmapFactory.Options options, String path) {
        boolean honeycomb = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        if (honeycomb && bitmap != options.inBitmap && options.inBitmap != null) {
            options.inBitmap.recycle();
            options.inBitmap = null;
        }
        if (bitmap == null){
            try {
                bitmap = BitmapFactory.decodeFile(path,options);
            }catch (OutOfMemoryError e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private static int getSampleSize(String path, int width, int height) {
        int[] bounds = decodeBounds(path);
        return calculateSampleSize(bounds[0],bounds[1],width,height);
    }


    //计算缩放比
    private static int calculateSampleSize(int width, int height, int reqWidth, int reqHeight) {
        if (width <= 0 || height <= 0){
            return 1;
        }
        if (reqHeight <= 0 && reqWidth <= 0){
            return 1;
        }else if (reqHeight <= 0){
            reqHeight = (int)(reqWidth * height / width + 0.5f);
        }else if (reqWidth <= 0){
            reqWidth = (int)(reqHeight * width / height + 0.5f);
        }
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = heightRatio < widthRatio? heightRatio : widthRatio;
            if (inSampleSize == 0) {
                inSampleSize = 1;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static int[] getThumbnailDisplaySize(float width, float height, int maxEdge, int MinEdge) {
        if (width <= 0 | height <= 0){
            return new int[]{MinEdge,MinEdge};
        }
        float shorter;
        float longer;
        boolean widthIsShorter;

        //store
        if (height < width) {
            shorter = height;
            longer = width;
            widthIsShorter = false;
        } else {
            shorter = width;
            longer = height;
            widthIsShorter = true;
        }
        if (shorter < MinEdge) {
            float scale = MinEdge / shorter;
            shorter = MinEdge;
            if (longer * scale > maxEdge) {
                longer = maxEdge;
            } else {
                longer *= scale;
            }
        }
        else if (longer > maxEdge) {
            float scale = maxEdge / longer;
            longer = maxEdge;
            if (shorter * scale < MinEdge) {
                shorter = MinEdge;
            } else {
                shorter *= scale;
            }
        }

        //restore
        if (widthIsShorter) {
            width = shorter;
            height = longer;
        } else {
            width = longer;
            height = shorter;
        }
        return new int[]{(int)width,(int)height};
    }
}
