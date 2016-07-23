package com.alien.sms.adapter;

import com.alien.sms.R;
import com.alien.sms.bean.Conversation;
import com.alien.sms.dao.ContactDao;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleGroupContainsAdapter extends CursorAdapter {

	private Bitmap avatar;

	public SingleGroupContainsAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_singlethread_ingroup, null);
	}

	@SuppressLint("NewApi")
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder holder = getHolder(view);
		Conversation conversation = Conversation.createFromCursor(cursor);
		String name = ContactDao.getNameByAddress(context.getContentResolver(), conversation.getAddress());
		if (TextUtils.isEmpty(name)) {
			holder.tv_singleGroup_address.setText(conversation.getAddress()+" ("+conversation.getMsgCount()+")");
		} else {
			holder.tv_singleGroup_address.setText(name+" ("+conversation.getMsgCount()+")");
		}
		avatar = ContactDao.getInstace().displayByAddress(conversation.getAddress(), context);
		if (avatar != null) {
			holder.iv_avatar.setImageBitmap(avatar);
		}
		if (DateUtils.isToday(conversation.getDate())) {
			holder.tv_singleGroup_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
		} else {
			holder.tv_singleGroup_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
		}
		holder.tv_singleGroup_body.setText(conversation.getSnippet());
	}
	
	private ViewHolder getHolder(View view){
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	
	class ViewHolder{
		private TextView tv_singleGroup_body;
		private TextView tv_singleGroup_address;
		private ImageView iv_avatar;
		private TextView tv_singleGroup_date;

		public ViewHolder(View view){
			iv_avatar = (ImageView) view.findViewById(R.id.iv_singlegroup_thread_avatar);
			tv_singleGroup_address = (TextView) view.findViewById(R.id.tv_single_group_address);
			tv_singleGroup_body = (TextView) view.findViewById(R.id.tv_singlegroup_body);
			tv_singleGroup_date = (TextView) view.findViewById(R.id.tv_singlegroup_thread_date);
		}
	}
}
