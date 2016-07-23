package com.alien.sms.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.alien.sms.R;
import com.alien.sms.adapter.GroupAdapter;
import com.alien.sms.base.BaseFragment;
import com.alien.sms.dao.GroupDao;
import com.alien.sms.dao.SimQueryHandler;
import com.alien.sms.dialog.CreateGroupDialog;
import com.alien.sms.dialog.CreateGroupDialog.OnCreateConfirmListener;
import com.alien.sms.dialog.ListDialog;
import com.alien.sms.dialog.ListDialog.OnListDialogListener;
import com.alien.sms.global.Constant;
import com.alien.sms.ui.activity.SingleGroupActivity;
import com.alien.sms.utils.ToastUtils;

public class GroupFragment extends BaseFragment {

	private Button bt_gc;
	private ListView lv;
	private GroupAdapter adapter;
	private SimQueryHandler handler;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_group, null);
		bt_gc = (Button) view.findViewById(R.id.bt_group_create);
		lv = (ListView) view.findViewById(R.id.lv_group_list);
		return view;
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		bt_gc.setOnClickListener(this);
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				final int group_id = cursor.getInt(cursor.getColumnIndex("_id"));

				ListDialog.showDialog(getContext(), "选择操作", new String[]{"修改","删除"}, new OnListDialogListener() {
					@Override
					//���á�ѡ���������Ŀ����¼�������position�ж�ʵ�ֺ��ֹ���
					public void OnItemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position) {
						case 0:
							CreateGroupDialog.showDialog(getContext(), "修改群组名称", new OnCreateConfirmListener() {
								@Override
								public void onConfirm(String groupName) {
									GroupDao.updateGroupName(getContext().getContentResolver(), groupName, group_id);	
								}							
								@Override
								public void onCancel() {	
								}
							});
							break;
						case 1:
							GroupDao.deleteGroup(getContext().getContentResolver(), group_id);	
							break;
						}					
					}
				});
				return true;
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Cursor cursor = (Cursor) adapter.getItem(position);
				if(cursor.getInt(cursor.getColumnIndex("thread_count")) == 0)
					ToastUtils.showToast(getContext(), "该分组还没有会话");
				else {
					Intent singleGroupIt = new Intent(getContext(), SingleGroupActivity.class);
					singleGroupIt.putExtra("group_name", cursor.getString(cursor.getColumnIndex("name")));
					singleGroupIt.putExtra("group_id", cursor.getInt(cursor.getColumnIndex("_id")));
					startActivity(singleGroupIt);
				}
			}
		});
	}

	@Override
	public void initData() {
		adapter = new GroupAdapter(getActivity(), null);
		lv.setAdapter(adapter);
		handler = new SimQueryHandler(getContext().getContentResolver());
		handler.startQuery(0, adapter, Constant.URI.GROUP, null, null, null, "create_date desc");
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_group_create:
			CreateGroupDialog.showDialog(getContext(), "新建群组",new OnCreateConfirmListener() {

				public void onConfirm(String groupName) {
					// TODO Auto-generated method stub
					if (TextUtils.isEmpty(groupName)) {
						ToastUtils.showToast(getContext(), "群组名不能为空");
					}else{
						GroupDao.insertGroup(getContext().getContentResolver(), groupName);
					}
				}		
				public void onCancel() {
				}
			});
			break;
		}
	}
}
