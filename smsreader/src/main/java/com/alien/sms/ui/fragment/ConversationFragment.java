package com.alien.sms.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alien.sms.R;
import com.alien.sms.adapter.ConversationsAdapter;
import com.alien.sms.base.BaseFragment;
import com.alien.sms.bean.Conversation;
import com.alien.sms.dao.GroupDao;
import com.alien.sms.dao.SimQueryHandler;
import com.alien.sms.dao.ThreadGroupDao;
import com.alien.sms.dialog.ConfirmDialog;
import com.alien.sms.dialog.ConfirmDialog.OnConfirmListener;
import com.alien.sms.dialog.ListDialog;
import com.alien.sms.dialog.ListDialog.OnListDialogListener;
import com.alien.sms.dialog.MsgDeletingDialog;
import com.alien.sms.dialog.MsgDeletingDialog.OnDeletingCancelListener;
import com.alien.sms.global.Constant;
import com.alien.sms.ui.activity.ConversationDetailActivity;
import com.alien.sms.ui.activity.NewMsgActivity;
import com.alien.sms.utils.ToastUtils;

import java.util.List;

public class ConversationFragment extends BaseFragment {

	private Button bt_edit_selectall;
	private Button bt_edit_unselect;
	private Button bt_edit_delete;
	private Button bt_new_message;
	private Button bt_edit;
	private LinearLayout ll_conversation_menu;
	private LinearLayout ll_conversation_edit_menu;
	private ConversationsAdapter adapter;
	private ListView lv_conversation;
	private List<String> selectedConversationId;
	private boolean stopDeleting = false;
	
	static final int WHAT_DELETE_COMPLETE = 0;
	static final int WHAT_UPDATE_DELETE_PROGRESS = 1;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DELETE_COMPLETE:
				deletingDialog.dismiss();
				adapter.setFlag(false);
				adapter.notifyDataSetChanged();
				showMenu();
				break;
			case WHAT_UPDATE_DELETE_PROGRESS:
				deletingDialog.updateProgressAndTitle(msg.arg1);
				break;
			}
		}
	};
	private MsgDeletingDialog deletingDialog;
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_conversation, null);
		lv_conversation = (ListView) view.findViewById(R.id.lv_conversation);
		
		bt_edit = (Button) view.findViewById(R.id.bt_edit);
		bt_new_message = (Button) view.findViewById(R.id.bt_new_message);
		bt_edit_delete = (Button) view.findViewById(R.id.bt_edit_delete);
		bt_edit_selectall = (Button) view.findViewById(R.id.bt_edit_selectall);
		bt_edit_unselect = (Button) view.findViewById(R.id.bt_edit_unselect);
				
		ll_conversation_menu = (LinearLayout) view.findViewById(R.id.ll_conversation_menu);
		ll_conversation_edit_menu = (LinearLayout) view.findViewById(R.id.ll_conversation_edit_menu);
		return view;
	}

	@Override
	public void initListener() {
		bt_edit.setOnClickListener(this);
		bt_new_message.setOnClickListener(this);
		bt_edit_delete.setOnClickListener(this);
		bt_edit_selectall.setOnClickListener(this);
		bt_edit_unselect.setOnClickListener(this);
		
		lv_conversation.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (adapter.isFlag()) {
					adapter.selectSingle(position);
				}else{
					Intent conversationIt = new Intent(getActivity(), ConversationDetailActivity.class);
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation.createFromCursor(cursor);
					conversationIt.putExtra("address", conversation.getAddress());
					conversationIt.putExtra("thread_id", conversation.getThreadId());
					startActivity(conversationIt);
				}				
			}
		});
		lv_conversation.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (!GroupDao.hasGroup(getContext().getContentResolver())) {
					ToastUtils.showToast(getContext(), "当前没有群组，请先创建一个群组");
				}else{
					Cursor cursor = (Cursor) adapter.getItem(position);
					int thread_id = cursor.getInt(cursor.getColumnIndex("_id"));
					if(ThreadGroupDao.isInGroup(getContext().getContentResolver(), thread_id))
						showDeleteFromGroupConfirmDialog(thread_id);
					else {
						showWhichToAddDialog(thread_id);
					}
				}
				return true;
			}
		});
	}

	protected void showWhichToAddDialog(final int thread_id) {

		final Cursor cursor = GroupDao.getGroupCursor(getContext().getContentResolver());	
		String[] groups = new String[cursor.getCount()];
		while (cursor.moveToNext()) {
			groups[cursor.getPosition()]=cursor.getString(1);
		}
		ListDialog.showDialog(getContext(), "要加入哪个分组？", groups, new OnListDialogListener() {
			
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				cursor.moveToPosition(position);
				int group_id = cursor.getInt(0);
				boolean isSuccess = ThreadGroupDao.insertThead(getContext().getContentResolver(),thread_id,group_id);
				ToastUtils.showToast(getContext(), isSuccess?"已加入分组" : "加入分组失败！");
			}
		});
	}

	protected void showDeleteFromGroupConfirmDialog(final int thread_id) {

		String group_name = ThreadGroupDao.getGroupByThreadId(getContext().getContentResolver(), thread_id);

		if(group_name == null){
			ToastUtils.showToast(getContext(), "数据错误,加入的群组已删除，请重试！");
			return;
		}		
		ConfirmDialog.showDialog(getContext(), "提示", "该联系人会话已经加入["+group_name+"]分组，是否退出该分组？", new OnConfirmListener() {
			public void onConfirm() {
				// TODO Auto-generated method stub
				boolean isSuccess = ThreadGroupDao.deleteInGroup(getContext().getContentResolver(), thread_id);
				ToastUtils.showToast(getContext(), isSuccess?"成功退出分组" : "操作失败");
			}			
			public void onCancel() {
			}
		});
	}

	public void initData() {
		adapter = new ConversationsAdapter(getActivity(), null);
		lv_conversation.setAdapter(adapter);
		
		SimQueryHandler queryHandler = new SimQueryHandler(getActivity().getContentResolver());
		//�Զ����ѯ�ֶ�
		String[] projection = {
				"sms.body AS snippet",
				"sms.thread_id AS _id",
				"groups.msg_count AS msg_count",
				"address AS address",
				"date AS date"
		};
		//arg1��������Я��adapter�����첽�ص�ʱʹ��
		queryHandler.startQuery(0, adapter, Constant.URI.SMS_CONVERSATION, projection, null, null, "date desc");

	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_edit:
			showEditMenu();
			adapter.setFlag(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_new_message:
			Intent intent = new Intent(getActivity(), NewMsgActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_edit_selectall:
			adapter.selectAll();
			break;
		case R.id.bt_edit_unselect:
			showMenu();
			adapter.setFlag(false);
			adapter.cancelSelect();
			break;
		case R.id.bt_edit_delete:
			selectedConversationId = adapter.getSelectedConversationId();
			if (!selectedConversationId.isEmpty()) {
				showDeleteDialog();
			}
			break;
		}
	}

	private void showEditMenu(){
		ll_conversation_menu.animate().translationY(ll_conversation_menu.getHeight()).setDuration(200);
		new Handler().postDelayed(new Runnable() {			

			public void run() {
				ll_conversation_edit_menu.animate().translationY(0).setDuration(200);
			}
		}, 200);		
	}
	
	private void showMenu(){
		ll_conversation_edit_menu.animate().translationY(ll_conversation_edit_menu.getHeight()).setDuration(200);
		ll_conversation_menu.animate().translationY(0).setDuration(200);
	}
	
	private void deleteSms(){
		deletingDialog = MsgDeletingDialog.showDialog(getActivity(), selectedConversationId.size(), new OnDeletingCancelListener() {
			public void onCancel() {
				stopDeleting = true;
			}
		});
		new Thread(new Runnable() {
			public void run() {
				for (int j = 1; j < selectedConversationId.size()+1; j++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (stopDeleting) {
						stopDeleting = false;
						Thread.currentThread().interrupt();
					}
					getActivity().getContentResolver().delete(Constant.URI.SMS, "thread_id = "+selectedConversationId.get(j-1), null);
					Message msg = handler.obtainMessage(WHAT_UPDATE_DELETE_PROGRESS,j,0);
					handler.sendMessage(msg);
				}
				selectedConversationId.clear();
				handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
			}
		}).start();	
	}
	
	private void showDeleteDialog(){
		ConfirmDialog.showDialog(getActivity(), "提示", "你要删除这些短信吗", new OnConfirmListener() {
			public void onConfirm() {
				// TODO Auto-generated method stub
				deleteSms();			
			}
			public void onCancel() {
			}
		});
	}
}
