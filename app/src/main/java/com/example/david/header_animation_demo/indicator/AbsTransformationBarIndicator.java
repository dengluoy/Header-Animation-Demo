package com.example.david.header_animation_demo.indicator;

import android.widget.ListView;

import com.example.david.header_animation_demo.widget.ObservableListView;
import com.example.david.header_animation_demo.widget.ObservableScrollViewCallbacks;
import com.example.david.header_animation_demo.widget.ScrollState;

/**
 * Created by David on 2016/1/10.
 */
public abstract class AbsTransformationBarIndicator implements ObservableScrollViewCallbacks{
    private static String TAG = "AbsTransformationBarIndicator";

    // 初始状态，向上执行动画
    public static final int STATUS_BOTTOM_TO_TOP = 1 << 5;
    // 浮层状态，向下执行动画
    public static final int STATUS_TOP_TO_BOTTOM = 1 << 6;
    //当前准备状态 , 初始化是需要向上滑动状态
    protected int mCurrentStatus = STATUS_BOTTOM_TO_TOP;
    //当前位置
    protected int mCurrentPos;
    //初始位置
    protected int mStartPos;
    //终点位置
    protected int mEndPos;
    protected boolean isUnderTouch;

    //监听滑动事件的View
    protected ObservableListView mListView;

    /**
     *
     * @param adapterView  滑动的view
     */
    public AbsTransformationBarIndicator(ObservableListView adapterView) {
        mListView = adapterView;
        mListView.setScrollViewCallbacks(this);
    }

    abstract void onDownEvent();

    abstract void onScrollChangeg(int scrollY, boolean firstScroll);

    abstract void onUpCancelEvent(ScrollState scrollState);

    @Override
    public void onDownMotionEvent() {
        onDownEvent();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        onScrollChangeg(scrollY, firstScroll);
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        onUpCancelEvent(scrollState);
    }
}
