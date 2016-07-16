package com.alienleeh.mylibrary.customUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.alienleeh.mylibrary.R;
import com.alienleeh.mylibrary.test.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

/**
 * Created by AlienLeeH <br/>
 * on 2016/7/25..Hour:12<br/>
 * Email:alienleeh@foxmail.com<br/>
 * Description:聊天界面用带进度ImageView
 */
public class ProgressImageView extends ImageView{

    private Context mContext;
    private Paint mPaint;
    private int progress;
    private float radius;
    private Rect textBounds;
    private static final Paint maskPaint = createMaskPaint();
    private Drawable mask = getResources().getDrawable(R.drawable.nim_message_item_round_bg);

    private static final Paint createMaskPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        return paint;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public ProgressImageView(Context context) {
        this(context,null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        radius = Utils.dp2px(mContext,15);
        textBounds = new Rect();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (mask != null) {
            // bounds


            // create blend layer
            canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

            //
            // mask
            //
            if (mask != null) {
                mask.setBounds(0, 0, width, height);
                mask.draw(canvas);
            }

            //
            // source
            //
            {
                canvas.saveLayer(0, 0, width, height, maskPaint, Canvas.ALL_SAVE_FLAG);
                super.onDraw(canvas);
                canvas.restore();
            }

            // apply blend layer
            canvas.restore();
        }else {
            super.onDraw(canvas);
        }
        //画阴影矩形
        mPaint.setColor(Color.parseColor("#70000000"));
        RectF rect = new RectF(0,0,getWidth(),getHeight()-getHeight()*progress/100);
        canvas.drawRoundRect(rect,radius,radius,mPaint);
        //打印进度数字
        if(progress != 100){
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(45);
            mPaint.getTextBounds("100%",0,"100%".length(), textBounds);
            mPaint.setStrokeWidth(2);
            canvas.drawText(progress+"%",
                    (float)getWidth()/2 - (float)textBounds.width()/2,
                    (float)getHeight() / 2 + textBounds.height() /2,
                    mPaint);
        }
    }

    public void loadAsAsset(String locationImage, DisplayImageOptions options) {
        String apath = ImageDownloader.Scheme.ASSETS.wrap(locationImage);
        ImageLoader.getInstance().displayImage(apath,this,options);
    }

    public void loadAsPath(String thumbpath, DisplayImageOptions options) {
        String url = ImageDownloader.Scheme.FILE.wrap(thumbpath);
        ImageLoader.getInstance().displayImage(url,this,options);
    }

    public void loadAsResource(int default_img) {
        setImageDrawable(getResources().getDrawable(default_img));
    }
}
