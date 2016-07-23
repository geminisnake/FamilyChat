package com.alien.sms.ui.activity;

import com.alien.sms.R;
import com.alien.sms.adapter.ConversationDetailAdapter;
import com.alien.sms.base.BaseActivity;
import com.alien.sms.dao.ContactDao;
import com.alien.sms.dao.SimQueryHandler;
import com.alien.sms.dao.SmsDao;
import com.alien.sms.global.Constant;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationDetailActivity extends BaseActivity {

	private ImageView iv_back_detail;
	private Button bt_send;
	private EditText et_typemessage;
	private String address = null;
	private String thread_id = null;
	private TextView tv_conversationdetail_title;
	private String name;
	private ListView lv_conversationdetail;

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_conversationdetail);
		iv_back_detail = (ImageView) findViewById(R.id.iv_back_detail);
		bt_send = (Button) findViewById(R.id.bt_sendmessage);
		tv_conversationdetail_title = (TextView) findViewById(R.id.tv_conversationdetail_title);
		lv_conversationdetail = (ListView) findViewById(R.id.lv_conversationdetail);
		lv_conversationdetail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		iv_back_detail.setOnClickListener(this);
		bt_send.setOnClickListener(this);
		et_typemessage = (EditText) findViewById(R.id.et_typemessage);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		
		if (intent != null) {
			address = intent.getStringExtra("address");
			name = ContactDao.getNameByAddress(getContentResolver(),address);
			thread_id = intent.getStringExtra("thread_id");
		}
		if (name != null) {
			tv_conversationdetail_title.setText(name);
		}else
			tv_conversationdetail_title.setText(address);
		
		ConversationDetailAdapter adapter = new ConversationDetailAdapter(this, null,lv_conversationdetail );
		lv_conversationdetail.setAdapter(adapter);
		String[] projection = {
				"body AS body",
				"type AS type",
				"_id",
				"date AS date"
				};
		SimQueryHandler simQueryHandler = new SimQueryHandler(getContentResolver());
		simQueryHandler.startQuery(0, adapter, Constant.URI.SMS, projection, "thread_id = ?", new String[]{thread_id}, "date asc");
		lv_conversationdetail.setSelection(lv_conversationdetail.getBottom());
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back_detail:
			finish();
			break;
		case R.id.bt_sendmessage:
			String sendingMessage = et_typemessage.getText().toString();
			if (!TextUtils.isEmpty(sendingMessage)) {
				SmsDao.sendSms(address,sendingMessage, this);
				et_typemessage.setText("");
			}
			break;
		}
	}
}
