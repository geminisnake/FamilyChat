package com.alien.sms.cache;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgMemoryCache implements ImageCache{
	LruCache<String, Bitmap> cache;
	
	public ImgMemoryCache() {
		final int maxSize = (int) (Runtime.getRuntime().maxMemory() / (1024*4));
		cache = new LruCache<String, Bitmap>(maxSize){
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight() / 1024;
			}
		};
	}

	@Override
	public Bitmap get(String address) {
		return cache.get(address);
	}

	@Override
	public void put(String address, Bitmap bitmap) {
		if (address != null & bitmap != null) {
			cache.put(address, bitmap);
		}
	}
}
class DiscImgCache implements ImageCache{
	static String cacheDir = "sdcard/com.alien.sms/avatarcache/";
	@Override
	public Bitmap get(String address) {
		// TODO Auto-generated method stub
		return BitmapFactory.decodeFile(cacheDir+address);
	}

	@Override
	public void put(String address, Bitmap bitmap) {
		FileOutputStream fos  = null;
		try {
			fos = new FileOutputStream(new File(cacheDir+address));
			bitmap.compress(CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}
