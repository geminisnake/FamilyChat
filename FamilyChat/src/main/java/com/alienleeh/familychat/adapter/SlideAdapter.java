package com.alienleeh.familychat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.alienleeh.familychat.R;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class SlideAdapter extends CursorAdapter{
    public SlideAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.item_list_slidingmenu,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHodler hodler = getHolder(view);
        hodler.tv1_inlist_slide.setText(cursor.getString(0));
        hodler.tv2_inlist_slide.setText(cursor.getString(1));
    }
    private ViewHodler getHolder(View view){
        ViewHodler hodler = (ViewHodler) view.getTag();
        if(hodler == null){
            hodler = new ViewHodler(view);
            view.setTag(hodler);
        }
        return hodler;
    }
    class ViewHodler{
        TextView tv1_inlist_slide;
        TextView tv2_inlist_slide;
        public ViewHodler(View view) {
            tv1_inlist_slide =  (TextView) view.findViewById(R.id.tv1_inlist_slide);
            tv2_inlist_slide =  (TextView) view.findViewById(R.id.tv2_inlist_slide);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
