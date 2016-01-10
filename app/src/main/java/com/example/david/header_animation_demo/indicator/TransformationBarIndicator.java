package com.example.david.header_animation_demo.indicator;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.ScrollerCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.david.header_animation_demo.R;
import com.example.david.header_animation_demo.widget.ObservableListView;
import com.example.david.header_animation_demo.widget.ScrollState;


/**
 * Created by David on 2016/1/10.
 */
public class TransformationBarIndicator extends AbsTransformationBarIndicator {

    private static String TAG = "TransformationBarIndicator";
    private LinearLayout mStartActionBarSearch;
    private LinearLayout mEndActionBarSearch;
    private View mHeaderView;
    private View mAnimotorView;
    private View mAlphaLayout;
    private View mLogoView;
    private View mHeadViewTopLayout;
    private Context mContext;
    private int mWidthScaleDistance;
    private int mHeightScaleDistance;
    private int mTargetViewWidth;
    private int mStartViewWidth;
    private int mTargetViewHegiht;
    private int mStartViewHeight;
    private int mDistance;
    private int mScrollY;
    private Handler mHandler = new Handler();
    private boolean isAnimation;

    public TransformationBarIndicator(Context context, @NonNull ObservableListView listView, @NonNull View headerView, @NonNull View animotorView) {
        super(listView);
        mContext = context;
        mHeaderView = headerView;
        mAnimotorView = animotorView;
        internalInit();
    }

    private void internalInit() {

        if(mAnimotorView==null)

        {
            throw new IllegalArgumentException("执行动画控件为空");
        }

        if(mHeaderView==null)

        {
            throw new IllegalArgumentException("ListView头为空");
        }

        mEndActionBarSearch=(LinearLayout)mAnimotorView.findViewById(R.id.temp_actionbar_search_animator);
        mStartActionBarSearch=(LinearLayout)mAnimotorView.findViewById(R.id.actionbar_search_animator);
        mAlphaLayout=mAnimotorView.findViewById(R.id.refresh_head_background_animator);
        mLogoView=mAnimotorView.findViewById(R.id.tujia_logo_animator);
        mHeadViewTopLayout = mHeaderView.findViewById(R.id.refresh_head_top_layout);

        mAnimotorView.getViewTreeObserver().

        addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                      @Override
                                      public void onGlobalLayout() {

                                          mTargetViewWidth = mEndActionBarSearch.getMeasuredWidth();
                                          mTargetViewHegiht = mEndActionBarSearch.getMeasuredHeight();
                                          mStartViewWidth = mStartActionBarSearch.getMeasuredWidth();
                                          mStartViewHeight = mStartActionBarSearch.getMeasuredHeight();

                                          if (mTargetViewWidth <= 0 && mStartViewWidth <= 0 && mTargetViewHegiht <= 0 && mStartViewHeight <= 0) {
                                              Log.d(TAG, "mTargetWidth : " + mTargetViewWidth);
                                              return;
                                          }

                                          int[] location = new int[2];
                                          mStartActionBarSearch.getLocationOnScreen(location);
                                          int startX = location[0];
                                          int startY = location[1];
                                          mEndActionBarSearch.getLocationOnScreen(location);
                                          int endX = location[0];
                                          int endY = location[1];
                                          mStartPos = startY;
                                          mEndPos = endY;
                                          mCurrentPos = mStartPos;

                                          mDistance = Math.abs(mStartPos - mEndPos);
                                          mWidthScaleDistance = Math.abs(mStartViewWidth - mTargetViewWidth);
                                          mHeightScaleDistance = Math.abs(mStartViewHeight - mTargetViewHegiht);
                                          Log.d(TAG, "mStartPos : " + mStartPos + ", mEndPos : " + mEndPos + ", mDistance : " + mDistance + ", mTargetWidth : " + mTargetViewWidth + ", mStartWidth : " + mStartViewWidth);
                                          Log.d(TAG, "HeaderViewTop : " + mHeaderView.getTop());
                                          removeOnGlobalLayoutListener(mAnimotorView, this);
                                      }
                                  }
        );
    }
    private void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if(view != null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
            } else {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            }
        }
    }

    private void performAnimation(ScrollState scrollState) {
        int currentY = (int) (mStartPos + mStartActionBarSearch.getTranslationY());
        int dy = 0;
        switch (scrollState) {
            case DOWN:
                if(currentY == mStartPos) {
                    break;
                }
            case UP:
                //永远大的减小的。 mEndPos 是最小的
                dy =  currentY - mEndPos;
                isAnimation = true;
                mHandler.post(new MoveActionTask(dy));
                break;
        }
    }

    private void tryToPerformMove(int scrollY) {

        mScrollY = scrollY;
        float change = Math.min(Math.max(scrollY, 0),mDistance);
        float ratio = change / mDistance;
        //背景动画
        transformationBgAlpha(ratio);
        //Logo动画
        transformationLogoVisible(ratio);
        //搜索框动画
        performMoveAndScaleToAnimation(scrollY, ratio);

        if(mScrollY <= 0) {
            mHeadViewTopLayout.setVisibility(View.VISIBLE);
            mAnimotorView.setVisibility(View.INVISIBLE);
        }else {
            mHeadViewTopLayout.setVisibility(View.GONE);
            mAnimotorView.setVisibility(View.VISIBLE);
        }
    }

    private void performMoveAndScaleToAnimation(int scrollY, float ratio) {

        performToScaleAnimation(ratio);

        int change = -scrollY;
        if(Math.abs(change) > mDistance) {
            change = -mDistance;
        }
        mStartActionBarSearch.setTranslationY(change);
    }

    private void performToScaleAnimation(float ratio) {
        int transformationWidth = (int) (ratio * mWidthScaleDistance);
        int transformationHeight = (int) (ratio * mHeightScaleDistance);

        transformationWidth = mStartViewWidth - transformationWidth;
        transformationHeight = mStartViewHeight - transformationHeight;
        RelativeLayout.LayoutParams targetLayoutParams = (RelativeLayout.LayoutParams) mStartActionBarSearch.getLayoutParams();
        targetLayoutParams.width = transformationWidth;
        targetLayoutParams.height = transformationHeight;
        mStartActionBarSearch.setLayoutParams(targetLayoutParams);
    }

    private void transformationBgAlpha(float ratio) {
        int alpha = (int) ((1.f - ratio) * 255);
        ((AlphaChangeLayout) mAlphaLayout).setBackgroundAlpha(alpha);
    }

    private void transformationLogoVisible(float ratio) {
        mLogoView.setAlpha(1.f - ratio);
        mLogoView.setScaleX(1.f - ratio);
        mLogoView.setScaleY(1.f - ratio);
    }

    @Override
    void onDownEvent() {}

    @Override
    void onScrollChangeg(int scrollY, boolean firstScroll) {
        //滑动动画
        tryToPerformMove(scrollY);
    }


    @Override
    void onUpCancelEvent(ScrollState scrollState) {
        Log.d(TAG, "scrollState : " + scrollState );
        performAnimation(scrollState);
    }

    private class MoveActionTask implements Runnable {

        private int distance;
        private int scrollSum = 0;
        private int defaultScroll = 10;
        public MoveActionTask(int distance) {
            this.distance = distance;
        }

        @Override
        public void run() {
            if(scrollSum + defaultScroll > distance) {
                defaultScroll = distance - scrollSum;
            }
            mListView.scrollListBy(defaultScroll);
            scrollSum += defaultScroll;
            /**
             * 自动动画过程
             */
            if(scrollSum < distance) {
                mHandler.post(this);
            }else {
                isAnimation = false;
            }
        }
    }
}
