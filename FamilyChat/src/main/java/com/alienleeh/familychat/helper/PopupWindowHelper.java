package com.alienleeh.familychat.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/7/19..Hour:01
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class PopupWindowHelper {

    public static PopupWindow getMsgHandlePopup(Context context, int width, int height, final MsgHandleListener listener) {

        View contentView = View.inflate(context, R.layout.popup_detailhandle,null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        contentView.findViewById(R.id.pop_msg_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete();
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.pop_msg_forward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onForward();
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    public interface MsgHandleListener {
        void onForward();
        void onDelete();
    }
}
