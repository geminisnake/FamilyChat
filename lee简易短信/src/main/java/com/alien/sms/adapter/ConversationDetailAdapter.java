package com.alien.sms.adapter;

import com.alien.sms.R;
import com.alien.sms.bean.ConversationDetail;
import com.alien.sms.dialog.ConfirmDialog;
import com.alien.sms.dialog.ConfirmDialog.OnConfirmListener;
import com.alien.sms.global.Constant;
import com.alien.sms.utils.CursorUtils;
import com.alien.sms.utils.LogUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationDetailAdapter extends CursorAdapter{
	
	private int type;
	private String body;
	private long date;
	static final long DURATION = 5 * 60 * 1000;
	private static final int TYPE_MSG_RECEIVED = 1;
	private static final int TYPE_MSG_SENT = 2;
	private ListView lv;
	private long _id;
	public Context mcontext;
	public ConversationDetailAdapter(Context context, Cursor cursor,ListView lv) {
		super(context, cursor);
		// TODO Auto-generated constructor stub
		this.lv = lv;
		this.mcontext = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		return View.inflate(context,R.layout.item_conversation_detail, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		DetailViewHolder viewHolder = getViewHolder(view);
		ConversationDetail detail = ConversationDetail.createFromCursor(cursor);
		type = detail.getTYPE();
		body = detail.getMsgBody();
		date = detail.getDate();
		_id = detail.get_id();
		switch (type) {
		case TYPE_MSG_RECEIVED:
			viewHolder.tv_sms_received.setText(body);
			viewHolder.tv_sms_send.setVisibility(View.GONE);
			viewHolder.tv_sms_received.setVisibility(View.VISIBLE);
			break;
		case TYPE_MSG_SENT:
			viewHolder.tv_sms_send.setText(body);
			viewHolder.tv_sms_send.setVisibility(View.VISIBLE);
			viewHolder.tv_sms_received.setVisibility(View.GONE);	
			break;
		}
		if (cursor.getPosition() == 0) 
			showDate(context,viewHolder);
		else {
			long predate = getPreviousDate(cursor.getPosition());
			if (date - predate > DURATION) {
				viewHolder.tv_sms_time.setVisibility(View.VISIBLE);
				showDate(context, viewHolder);
			}else {
				viewHolder.tv_sms_time.setVisibility(View.GONE);
			}
		}
	}
	private DetailViewHolder getViewHolder(View v){
		DetailViewHolder dviewHolder = (DetailViewHolder) v.getTag();
		if (dviewHolder == null) {
			dviewHolder = new DetailViewHolder(v);
			v.setTag(dviewHolder);
		}
		return dviewHolder;
	}
	class DetailViewHolder{
		
		private TextView tv_sms_received;
		private TextView tv_sms_send;
		private TextView tv_sms_time;

		public DetailViewHolder(View v){
			tv_sms_received = (TextView) v.findViewById(R.id.tv_sms_received);
			tv_sms_send = (TextView) v.findViewById(R.id.tv_sms_send);
			tv_sms_time = (TextView) v.findViewById(R.id.tv_sms_time);
		}
	}
	public void showDate(Context context,DetailViewHolder viewHolder){
		if (DateUtils.isToday(date)) {
			viewHolder.tv_sms_time.setText(DateFormat.getTimeFormat(context).format(date));
		}else {
			viewHolder.tv_sms_time.setText(DateFormat.getDateFormat(context).format(date));
		}
	}
	private long getPreviousDate(int position){
		Cursor cursor = (Cursor) getItem(position-1);
		ConversationDetail conversationDetail = ConversationDetail.createFromCursor(cursor);
		return conversationDetail.getDate();
	}
	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
		lv.setSelection(cursor.getCount()-1);
	}

}