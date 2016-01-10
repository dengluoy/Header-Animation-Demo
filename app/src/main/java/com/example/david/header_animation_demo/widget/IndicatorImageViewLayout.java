package com.example.david.header_animation_demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.david.header_animation_demo.R;

public class IndicatorImageViewLayout extends RelativeLayout {
	private int mIndicatorSize;
	private int mIndicatorColor;
	private int mImageHeight;
	private int mImageWidth;
	private Paint mPaint;
	private ImageView mImageView;
	private int mIndicatorX;
	private int mIndicatorY;
	private boolean mShowIndicator = false;
	// 圆点位置是否依赖图片内容，默认true
	private boolean mAttachToImage = true;

	public IndicatorImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.indicator_view);
		mIndicatorSize = a.getDimensionPixelSize(R.styleable.indicator_view_corner_indicator_size, 0);
		mIndicatorColor = a.getColor(R.styleable.indicator_view_corner_indicator_color, 0);
		mAttachToImage = a.getBoolean(R.styleable.indicator_view_attach_to_image, true);
		a.recycle();

		init();
	}

	public IndicatorImageViewLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public IndicatorImageViewLayout(Context context) {
		this(context, null, 0);
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setColor(mIndicatorColor);
		mPaint.setStyle(Style.FILL);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mImageView = (ImageView) findViewById(R.id.imageview);
	}

	public void showIndicator(boolean show) {
		if (mShowIndicator != show) {
			mShowIndicator = show;
			invalidate();
		}
	}

	public boolean isIndicatorShowing() {
		return mShowIndicator;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mShowIndicator) {
			if (mAttachToImage) {
				BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
				mImageWidth = drawable.getIntrinsicWidth();
				mImageHeight = drawable.getIntrinsicHeight();
				int mImageViewSrcHeight = (int) (mImageView.getWidth() * ((float) mImageHeight / mImageWidth));
				mIndicatorX = mImageView.getRight();
				mIndicatorY = mImageView.getTop() + (mImageView.getHeight() - mImageViewSrcHeight) / 2;
				canvas.drawCircle(mIndicatorX - 0.5f, mIndicatorY + 0.5f, mIndicatorSize / 2, mPaint);
			} else {
				float indicatorX = (float) (getWidth() / 2
						+ ((getHeight() / 2 - mIndicatorSize / 2) * Math.sqrt(3) / 2.0f)) - 0.5f;
				float indicatorY = (float) (getHeight() / 2 - (getHeight() / 2 - mIndicatorSize / 2) / 2) + 0.5f;
				canvas.drawCircle(indicatorX - 0.5f, indicatorY + 0.5f, mIndicatorSize / 2, mPaint);
			}
		}
	}
}
