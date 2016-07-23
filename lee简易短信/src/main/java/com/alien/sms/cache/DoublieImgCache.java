package com.alien.sms.cache;

import android.graphics.Bitmap;

public class DoublieImgCache implements ImageCache{
	ImgMemoryCache mCache = new ImgMemoryCache();
	DiscImgCache dCache = new DiscImgCache();
	@Override
	public Bitmap get(String address) {
		Bitmap bitmap = mCache.get(address);
		if (bitmap == null) {
			bitmap = dCache.get(address);
			if (bitmap != null) {
				mCache.put(address, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void put(String address, Bitmap bitmap) {
		mCache.put(address, bitmap);
		dCache.put(address, bitmap);
		
	}

}
