package com.alien.sms.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alien.sms.R;
import com.alien.sms.base.BaseDialog;

public class ListDialog extends BaseDialog {

	private TextView tv_title;
	private ListView lv_function;
	private String title;
	private String[] functions;
	private OnListDialogListener listener;
	private Context mContext;
	public static void showDialog(Context context,String title,String[] functions,OnListDialogListener listener){
		ListDialog dialog = new ListDialog(context, title, functions, listener);
		dialog.show();
	}
	protected ListDialog(Context context,String title,String[] functions,OnListDialogListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.functions = functions;
		this.listener = listener;
		mContext = context;
	}

	@Override
	public void processClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.dialog_list_operate);
		tv_title = (TextView) findViewById(R.id.tv_listdialog_title);
		lv_function = (ListView) findViewById(R.id.lv_listdialog);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		lv_function.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(listener != null)
					listener.OnItemClick(parent,view,position,id);
				dismiss();
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tv_title.setText(title);
		lv_function.setAdapter(new MyAdapter());
	}
	public interface OnListDialogListener{
		void OnItemClick(AdapterView<?> parent, View view, int position, long id);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return functions.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if(convertView == null)
				view = View.inflate(getContext(), R.layout.item_function_listdialog, null);
			else
				view = convertView;
			TextView tv_name = (TextView) view.findViewById(R.id.tv_functionname_inlistdialog);
			tv_name.setText(functions[position]);
			return view;
		}
	}
}
