package com.alien.sms.cache;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.widget.ImageView;

import com.alien.sms.utils.LogUtils;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImgManager {
	private int currentType = 0;

	private ImgManager() {
	}
	private static ImgManager instance= null;
	public static ImgManager getInstance() {
		if (instance == null) {
			synchronized (ImgManager.class) {
				if (instance == null) {
					instance = new ImgManager();
				}
			}
		}
		return instance;
	}
	ImageCache cache = new ImgMemoryCache();
	Bitmap defaultb = null;
	public void setDefault(Bitmap bitmap) {
		if (this.defaultb == null || !this.defaultb.equals(bitmap))
		this.defaultb = bitmap;
	}
	ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public void setCache(int cache) {
		if (currentType == cache){
			return;
		}else {
			switch (cache){
				case 0:
					this.currentType = 0;
					this.cache = new ImgMemoryCache();
					break;
				case 1:
					this.currentType = 1;
					this.cache = new DiscImgCache();
					break;
				case 2:
					this.currentType = 2;
					this.cache = new DoublieImgCache();
					break;
			}
		}
	}
	public void displayImg(ImageView view,String address,boolean def){
		Bitmap bitmap = cache.get(address);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			return;
		}
		submitLoadRequest(address,view,def);
	}
	private void submitLoadRequest(final String address, final ImageView view, final boolean def) {
		view.setTag(address);
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap2 = findBitMap(address);
				if (bitmap2 == null ) {
					if (def) {
						view.setImageBitmap(defaultb);
					}
				}
				if (bitmap2 != null & view.getTag().equals(address)) {
					view.setImageBitmap(bitmap2);
					cache.put(address, bitmap2);
				}
			}
		});
	}

			private Bitmap findBitMap(String address) {
				Bitmap avatar = null;
				Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
				ContentResolver cr =  LogUtils.getContext().getContentResolver();
				Cursor cursor =cr.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
				if (cursor.moveToFirst()) {
					String _id = cursor.getString(0);
					InputStream is = Contacts.openContactPhotoInputStream(cr, Uri.withAppendedPath(Contacts.CONTENT_URI, _id));
					avatar = BitmapFactory.decodeStream(is);
					cursor.close();
				}	
				return avatar;
			}
}
