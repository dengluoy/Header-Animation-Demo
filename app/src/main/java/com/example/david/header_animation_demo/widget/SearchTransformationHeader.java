package com.example.david.header_animation_demo.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.david.header_animation_demo.R;
import com.example.david.header_animation_demo.indicator.AlphaChangeLayout;


/**
 * Created by DW on 16/1/7.
 */
public class SearchTransformationHeader extends RelativeLayout implements AlphaChangeLayout {

    private Drawable mBackground;

    public SearchTransformationHeader(Context context) {
        super(context);
        internalInit();
    }

    public SearchTransformationHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        internalInit();
    }

    public SearchTransformationHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        internalInit();
    }

    private void internalInit() {
        mBackground = new ColorDrawable(getResources().getColor(R.color.bg_header_transformation));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(mBackground);
        } else {
            this.setBackgroundDrawable(mBackground);
        }
    }

    @Override
    public void setBackgroundAlpha(int alpha) {
        mBackground.setAlpha(alpha);
    }
}
