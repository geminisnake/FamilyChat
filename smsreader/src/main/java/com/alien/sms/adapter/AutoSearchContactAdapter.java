package com.alien.sms.adapter;

import com.alien.sms.R;
import com.alien.sms.dao.ContactDao;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoSearchContactAdapter extends CursorAdapter {
	LruCache<String, Bitmap> bitholder = null;
	private Bitmap bitmap;
	public AutoSearchContactAdapter(Context context, Cursor c) {
		super(context, c);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 4));
		bitholder = new LruCache<String, Bitmap>(maxMemory){
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight() /1024;
			}
		};
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_auto_search, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = getHolder(view);
		viewHolder.tv_name.setText(cursor.getString(cursor.getColumnIndex("display_name")));
		String address = cursor.getString(cursor.getColumnIndex("data1"));
		viewHolder.tv_address.setText(address);
		bitmap = bitholder.get(address);
		if (bitmap != null) {
			viewHolder.iv_avatar.setImageBitmap(bitholder.get(address));
		}else {
			bitmap = ContactDao.getAvatarByAddress(context.getContentResolver(), address);
			bitholder.put(address, bitmap);
		}
	}
	private ViewHolder getHolder(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	class ViewHolder{
		private TextView tv_name;
		private TextView tv_address;
		private ImageView iv_avatar;
		public ViewHolder(View v) {
			// TODO Auto-generated constructor stub
			tv_name = (TextView) v.findViewById(R.id.tv_auto_contact_name);
			tv_address = (TextView) v.findViewById(R.id.tv_auto_contact_address);
			iv_avatar = (ImageView) v.findViewById(R.id.iv_auto_contactimg);
		}
	}
	@Override
	public CharSequence convertToString(Cursor cursor) {
		// TODO Auto-generated method stub
		return cursor.getString(cursor.getColumnIndex("data1"));
	}
}
