package com.alien.sms.ui.activity;

import com.alien.sms.R;
import com.alien.sms.adapter.SingleGroupContainsAdapter;
import com.alien.sms.base.BaseActivity;
import com.alien.sms.bean.Conversation;
import com.alien.sms.dao.SimQueryHandler;
import com.alien.sms.dao.ThreadGroupDao;
import com.alien.sms.global.Constant;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SingleGroupActivity extends BaseActivity {

	private ListView lv_singlegroup_list;
	private TextView tv_conversationdetail_title;
	private String groupName = null;
	private ImageView iv_back_detail;
	private int groupId;
	private SingleGroupContainsAdapter adapter;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_singlegroup);
		tv_conversationdetail_title = (TextView) findViewById(R.id.tv_conversationdetail_title);
		lv_singlegroup_list = (ListView) findViewById(R.id.lv_singlegroup_list);
		iv_back_detail = (ImageView) findViewById(R.id.iv_back_detail);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		iv_back_detail.setOnClickListener(this);
		lv_singlegroup_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent detailIt = new Intent(SingleGroupActivity.this, ConversationDetailActivity.class);
				
				Cursor cursor = (Cursor) adapter.getItem(position);
				Conversation conversation = Conversation.createFromCursor(cursor);
				detailIt.putExtra("address", conversation.getAddress());
				detailIt.putExtra("thread_id", conversation.getThreadId());
				startActivity(detailIt);
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Intent it = getIntent();
		groupName = it.getStringExtra("group_name");
		groupId = it.getIntExtra("group_id", -1);
		tv_conversationdetail_title.setText(groupName);
		
		adapter = new SingleGroupContainsAdapter(this, null);
		lv_singlegroup_list.setAdapter(adapter);
		String[] projection = {
				"sms.body AS snippet",
				"sms.thread_id AS _id",
				"groups.msg_count AS msg_count",
				"address AS address",
				"date as date"
		};
		Cursor cursor = ThreadGroupDao.findAllThreadByGroup(getContentResolver(),groupId);
		StringBuilder sb = new StringBuilder("thread_id in (");
		while (cursor.moveToNext()) {
			if(cursor.isLast())
				sb.append(cursor.getInt(0)+")");
			else
				sb.append(cursor.getInt(0)+",");
		}
		SimQueryHandler handler = new SimQueryHandler(getContentResolver());
		handler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projection, sb.toString(), null, "date desc");
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back_detail:
			finish();
			break;

		}
	}

}
