package com.alien.sms.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alien.sms.R;
import com.alien.sms.bean.Conversation;
import com.alien.sms.cache.ImgManager;
import com.alien.sms.dao.ContactDao;

import java.util.ArrayList;
import java.util.List;

public class ConversationsAdapter extends CursorAdapter {
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isFlag() {
		return flag;
	}

	private boolean flag = false;
	
	List<String> selectedConversationId = new ArrayList<>();

    ImgManager manager;


    public List<String> getSelectedConversationId() {
		return selectedConversationId;
	}

	public ConversationsAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
		manager = ImgManager.getInstance();
		manager.setCache(2);
		
		Bitmap defaultss = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.img_default_avatar));
		manager.setDefault(defaultss);
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// ï¿½ï¿½ï¿?
		return View.inflate(context, R.layout.item_lv_conversation, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder holder = getHolder(view);
		//ï¿½ï¿½ï¿½ï¿½cursor
		Conversation conversation = Conversation.createFromCursor(cursor);
        String address = conversation.getAddress();
        String name = ContactDao.getNameByAddress(context.getContentResolver(), address);

		manager.displayImg(holder.iv_conversation_avatarphoto, address,true);
		
		if (flag) {
			holder.cb_conversation_delete.setVisibility(View.VISIBLE);
			if (selectedConversationId.contains(conversation.getThreadId())) {
				holder.cb_conversation_delete.setImageResource(R.drawable.common_checkbox_checked);
			}else
				holder.cb_conversation_delete.setImageResource(R.drawable.common_checkbox_normal);
		}else{
			holder.cb_conversation_delete.setVisibility(View.GONE);
		}
		holder.tv_conversation_body.setText(conversation.getSnippet());
		
		if (name != null) {
			holder.tv_conversation_address.setText(String.format("%s(%s)", name, conversation.getMsgCount()));
		}else {
			holder.tv_conversation_address.setText(String.format("%s(%s)", address, conversation.getMsgCount()));
		}		
		
		if(DateUtils.isToday(conversation.getDate())){
			//ï¿½ï¿½ï¿½ï¿½Ç£ï¿½ï¿½ï¿½Ê¾Ê±ï¿½ï¿?
			holder.tv_conversation_date.setText(DateFormat.getTimeFormat(context).format(conversation.getDate()));
		}
		else{
			//ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ç£ï¿½ï¿½ï¿½Ê¾ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
			holder.tv_conversation_date.setText(DateFormat.getDateFormat(context).format(conversation.getDate()));
		}
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
		private TextView tv_conversation_body;
		private TextView tv_conversation_address;
		private ImageView iv_conversation_avatarphoto;
		private TextView tv_conversation_date;
		private ImageView cb_conversation_delete;
		
		public ViewHolder(View view){
			iv_conversation_avatarphoto = (ImageView) view.findViewById(R.id.iv_conversation_avatarphoto);
			tv_conversation_address = (TextView) view.findViewById(R.id.tv_conversation_address);
			tv_conversation_body = (TextView) view.findViewById(R.id.tv_conversation_body);
			tv_conversation_date = (TextView) view.findViewById(R.id.tv_conversation_date);
			cb_conversation_delete = (ImageView) view.findViewById(R.id.cb_conversation_delete);
		}
	}
	
	public void selectSingle(int position){
		Cursor cursor = (Cursor) getItem(position);
		String id = cursor.getString(cursor.getColumnIndex("_id"));
		if (selectedConversationId.contains(id)) {
			selectedConversationId.remove(id);
		}else{
					selectedConversationId.add(id);
		}
		notifyDataSetChanged();
        cursor.close();
	}
	public void selectAll(){
		Cursor cursor = getCursor();
		selectedConversationId.clear();
		if (cursor.moveToFirst()) {
			do {
				selectedConversationId.add(cursor.getString(cursor.getColumnIndex("_id")));
			} while (cursor.moveToNext());
		}
		notifyDataSetChanged();
        cursor.close();
	}
	public void cancelSelect(){
		selectedConversationId.clear();
		notifyDataSetChanged();
	}
}
