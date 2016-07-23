package com.alien.sms.adapter;

import com.alien.sms.R;
import com.alien.sms.bean.Group;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GroupAdapter extends CursorAdapter {

	public GroupAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return View.inflate(context, R.layout.item_group, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = getHolder(view);
		Group group = Group.createGroupFromCursor(cursor);
		
		viewHolder.tv_name.setText(group.getName()+" ("+group.getThread_count()+")");
		
		if (DateUtils.isToday(group.getCreate_date())) {
			viewHolder.tv_date.setText(DateFormat.getTimeFormat(context).format(group.getCreate_date()));
		}else {
			viewHolder.tv_date.setText(DateFormat.getDateFormat(context).format(group.getCreate_date()));
		}
	}
	private ViewHolder getHolder(View v){
		ViewHolder holder = (ViewHolder) v.getTag();
		if (holder == null) {
			holder = new ViewHolder(v);
			v.setTag(holder);
		}
		return holder;
	}
	class ViewHolder{

		private TextView tv_name;
		private TextView tv_date;

		public ViewHolder(View v) {
			tv_name = (TextView) v.findViewById(R.id.tv_group_name);
			tv_date = (TextView) v.findViewById(R.id.tv_group_date);
		}
	}
}
