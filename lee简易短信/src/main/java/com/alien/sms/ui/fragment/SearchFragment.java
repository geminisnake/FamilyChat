package com.alien.sms.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alien.sms.R;
import com.alien.sms.adapter.ConversationsAdapter;
import com.alien.sms.base.BaseFragment;
import com.alien.sms.bean.Conversation;
import com.alien.sms.dao.SimQueryHandler;
import com.alien.sms.global.Constant;
import com.alien.sms.ui.activity.ConversationDetailActivity;

public class SearchFragment extends BaseFragment {

	private EditText et_search;
	private ListView lv_search;
	private SimQueryHandler queryHandler;
	private ConversationsAdapter adapter;
	String[] projection = {
			"sms.body AS snippet",
			"sms.thread_id AS _id",
			"groups.msg_count AS msg_count",
			"address AS address",
			"date AS date"
	};

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_search, null);
		et_search = (EditText) view.findViewById(R.id.et_search);
		lv_search = (ListView) view.findViewById(R.id.lv_search_list);
		return view;
	}

	@Override
	public void initListener() {

		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(s))
					queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projection, "body like '%"+s+"%'", null, "date desc");
				else {
					adapter.changeCursor(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent detailIt = new Intent(getContext(), ConversationDetailActivity.class);
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
		adapter = new ConversationsAdapter(getContext(), null);
		lv_search.setAdapter(adapter);
		queryHandler = new SimQueryHandler(getActivity().getContentResolver());
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.lv_search_list:
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		}
	}

}
