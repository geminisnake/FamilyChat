package com.alien.sms.ui.activity;

import com.alien.sms.R;
import com.alien.sms.adapter.AutoSearchContactAdapter;
import com.alien.sms.base.BaseActivity;
import com.alien.sms.dao.SmsDao;
import com.alien.sms.utils.ToastUtils;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

public class NewMsgActivity extends BaseActivity {

	private ImageView iv_back_detail;
	private Button bt_newmsg;
	private EditText et_typenewmsg;
	private ImageView iv_contact;
	private TextView tv_title;
	private AutoCompleteTextView act_address_sendto;

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_newmsg);
		iv_back_detail = (ImageView) findViewById(R.id.iv_back_detail);
		bt_newmsg = (Button) findViewById(R.id.bt_newmsg_sendmsg);
		act_address_sendto = (AutoCompleteTextView) findViewById(R.id.act_address_sendto);
		et_typenewmsg = (EditText) findViewById(R.id.et_typemsg_newmsg);
		iv_contact = (ImageView) findViewById(R.id.iv_newmsg_contact);
		tv_title = (TextView) findViewById(R.id.tv_conversationdetail_title);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		iv_back_detail.setOnClickListener(this);
		bt_newmsg.setOnClickListener(this);
		iv_contact.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tv_title.setText("新建短信");
		AutoSearchContactAdapter adapter = new AutoSearchContactAdapter(this, null);
		act_address_sendto.setAdapter(adapter);
		adapter.setFilterQueryProvider(new FilterQueryProvider() {			
			@Override
			public Cursor runQuery(CharSequence constraint) {
				// TODO Auto-generated method stub
				String[] projection = {
					"data1",
					"display_name",
					"_id"
				};
				String selection = "data1 like '%"+constraint+"%'";
				Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, projection, selection, null, "display_name");
				return cursor;
			}
		});
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back_detail:
			finish();
			break;
		case R.id.bt_newmsg_sendmsg:
			String address = act_address_sendto.getText().toString();
			String sendingMsg = et_typenewmsg.getText().toString();
			if (TextUtils.isEmpty(address))
				ToastUtils.showToast(this, "您尚未输入联系人号码!");
			else if (TextUtils.isEmpty(sendingMsg))
				ToastUtils.showToast(this, "短信内容为空!");
			else
				SmsDao.sendSms(address, sendingMsg, this);
			break;
		case R.id.iv_newmsg_contact:
			Intent it = new Intent(Intent.ACTION_PICK);
			it.setType("vnd.android.cursor.dir/contact");
			startActivityForResult(it, 0);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Uri ur = data.getData();
			String[] prStrings = {
			"_id",	
			"has_phone_number"
			};
			Cursor cursor = getContentResolver().query(ur, prStrings, null, null, null);
			cursor.moveToFirst();
			long _id = cursor.getLong(0);
			int isNumber = cursor.getInt(1);
			if (isNumber == 1) {			
				Cursor cursor2 = getContentResolver().query(Phone.CONTENT_URI, new String[]{"data1"}, "contact_id = "+_id, null, null);
				cursor2.moveToFirst();
				act_address_sendto.setText(cursor2.getString(cursor2.getColumnIndex("data1")));
				et_typenewmsg.requestFocus();
				cursor2.close();
			}else{
				ToastUtils.showToast(this, "该联系人没有号码");
				act_address_sendto.setText(null);
			}
			cursor.close();
		}

	}
}
