package com.alienleeh.familychat.emotions.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.emotions.bean.EmotionTab;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.utils.ApplicationUtils;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class EmotionTabAdapter extends RecyclerView.Adapter<EmotionTabAdapter.ViewHolder> {
    private List<EmotionTab> tabs;
    private Context context;
    private OnItemClickListener listener;
    LayoutInflater inflater;

    public EmotionTabAdapter(Context context, List<EmotionTab> tabs) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.tabs = tabs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_emotion_tab,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0){
            holder.tab_text.setVisibility(View.VISIBLE);
            holder.tab_text.setText("功能");
            holder.btn.setVisibility(View.GONE);
        }else {
            EmotionTab tab = tabs.get(position - 1);
            ImageLoaderHelper.displayAsset(holder.btn,tab.iconPath);
        }
        holder.tab_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v,position);
            }
        });

        float width = ApplicationUtils.getWidth() / 6;
        ViewGroup.LayoutParams params = holder.tab_container.getLayoutParams();
        params.width = (int) width;
        holder.tab_container.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        if (tabs == null){
            return 0;
        }
        return tabs.size() + 1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onClick(View view, int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView btn;
        TextView tab_text;
        FrameLayout tab_container;
        public ViewHolder(View itemView) {
            super(itemView);
            btn = (ImageView) itemView.findViewById(R.id.emotion_tab_btn);
            tab_text = (TextView) itemView.findViewById(R.id.emotion_tab_text_btn);
            tab_container = (FrameLayout) itemView.findViewById(R.id.emotion_tab_container);
        }
    }
}
