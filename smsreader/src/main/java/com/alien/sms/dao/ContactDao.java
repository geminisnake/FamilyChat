package com.alien.sms.dao;

import java.io.IOException;
import java.io.InputStream;

import com.alien.sms.utils.LogUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.LruCache;
import android.widget.ImageView;

public class ContactDao {
	
	LruCache<String, Bitmap> cache;
	private static ContactDao instace = null;
	
	private ContactDao() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024*4));
		cache = new LruCache<String, Bitmap>(maxMemory){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight() /1024;
			}
		};
	}
	public static ContactDao getInstace() {
		if (instace == null) {
			synchronized (ContactDao.class) {
				if (instace == null) {
					instace = new ContactDao();
				}
			}
		}
		return instace;
	}
	public synchronized Bitmap displayByAddress(String address,Context context) {
		if (cache.get(address) != null) {
			return cache.get(address);
		}else {
			Bitmap bitmap = getAvatarByAddress(context.getContentResolver(), address);
			if (bitmap != null) {
				cache.put(address, bitmap);
			}
			return bitmap;
		}
	}

	public static String getNameByAddress(ContentResolver contentResolver,String address){
		String name = null;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = contentResolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
			cursor.close();
		}
		return name;
	}
	
	public static Bitmap getAvatarByAddress(ContentResolver contentResolver,String address){
		Bitmap avatar = null;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = contentResolver.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
		if (cursor.moveToFirst()) {
			String _id = cursor.getString(0);
			
			InputStream is = Contacts.openContactPhotoInputStream(contentResolver, Uri.withAppendedPath(Contacts.CONTENT_URI, _id));
			avatar = BitmapFactory.decodeStream(is);
			if (avatar != null) {
				for (int i = 0; i < 20; i++) {
				LogUtils.e("hahahaha","¿íÊÇ"+LogUtils.dp2px(50)+"¡£¡£¡£¡£¸ßÊÇ"+avatar.getHeight());
			}
			cursor.close();
			}
		
		}
		return avatar;	
	}
}
