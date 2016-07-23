package com.alien.sms.global;

import com.alien.sms.provider.GroupProvider;

import android.net.Uri;

public class Constant {
	public interface URI{
		Uri SMS_CONVERSATION = Uri.parse("content://sms/conversations");
		Uri SMS = Uri.parse("content://sms");
		
		Uri GROUP = Uri.withAppendedPath(GroupProvider.BASE_URI, "/groups");
		Uri GROUP_INSERT = Uri.withAppendedPath(GroupProvider.BASE_URI, "/groups/insert");
		Uri GROUP_UPDATE = Uri.withAppendedPath(GroupProvider.BASE_URI, "/groups/update");
		Uri GROUP_DELETE = Uri.withAppendedPath(GroupProvider.BASE_URI, "/groups/delete");

		Uri THREAD_GROUP = Uri.withAppendedPath(GroupProvider.BASE_URI, "/thread_groups");
		Uri THREAD_GROUP_INSERT = Uri.withAppendedPath(GroupProvider.BASE_URI, "/thread_groups/insert");
		Uri THREAD_GROUP_UPDATE = Uri.withAppendedPath(GroupProvider.BASE_URI, "/thread_groups/update");
		Uri THREAD_GROUP_DELETE = Uri.withAppendedPath(GroupProvider.BASE_URI, "/thread_groups/delete");
	}
	public interface MSG_TYPE{
		int RECEIVED = 1;
		int SENT = 2;
	}
}
