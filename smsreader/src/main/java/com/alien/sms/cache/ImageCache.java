package com.alien.sms.cache;

import android.graphics.Bitmap;

public interface ImageCache {
	public Bitmap get(String address);
	public void put(String address,Bitmap bitmap);
}
