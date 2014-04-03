package com.slidinglistviewitem;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 
 * @author dupengtao88@gmail.com
 * 
 *         2014-4-2
 */
public class HorizontalScrollViewItem extends HorizontalScrollView {

	public interface ScrollViewListener {
		void onScrollChanged(HorizontalScrollViewItem scrollView, int x, int y,
				int oldx, int oldy);
	}

	private Context mContext;
	private View mView;
	private LinearLayout mLlFirstView, mLlSecondView, mLBodyView;
	private int offset;
	private GestureDetector gestureDetector;
	protected int msTate = -1;
	private HorizontalScrollViewItem mScrollView;
	private ScrollViewListener scrollViewListener = null;
	private int mY;
	private boolean isMove;
	private Runnable mRestCb;

	public HorizontalScrollViewItem(Context context, View itemFrist,
			View itemSecond) {
		super(context);
		mContext = context;
		mScrollView = HorizontalScrollViewItem.this;
		initXml(itemFrist, itemSecond);
		setEvents();
	}

	private void setEvents() {
		mSwipeDetectorCallBack = new SwipeDetectorCallBack() {
			@Override
			public void onToRight() {
				msTate = 1;
			}

			@Override
			public void onToLeft() {
				msTate = 2;
			}

			@Override
			public void onBackRight() {
				msTate = 3;
			}

			@Override
			public void onBackLeft() {
				msTate = 4;
			}
		};
		gestureDetector = new GestureDetector(mContext, new SwipeDetector(
				mSwipeDetectorCallBack));

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				int action = event.getAction();
				mY = (int) event.getY();
				switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP: {
					switch (msTate) {
					case 1: {// scroll to right
						scrollToRight();
					}
						break;
					case 2: {// scroll to left
						scrollToLeft();
					}
						break;
					case 3: {// back to right
						scrollToRight();
					}
						break;
					case 4: {// back to left
						scrollToLeft();
					}
						break;
					}
					msTate = -1;
				}
					break;
				}

				return false;

			}
		});

	}

	public void setRestCb(Runnable restCb) {
		this.mRestCb = restCb;
	}

	public boolean isMove() {
		return isMove;
	}

	private void initXml(View itemFrist, View itemSecond) {
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		this.setPadding(0, 15, 0, 15);
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.setFillViewport(true);
		mView = View.inflate(mContext, R.layout.item, this);
		mLBodyView = (LinearLayout) mView.findViewById(R.id.llContext);
		mLlFirstView = (LinearLayout) mView.findViewById(R.id.ll1);
		mLlSecondView = (LinearLayout) mView.findViewById(R.id.ll2);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenW = dm.widthPixels;
		// do not forget paddingLeft or paddingRight
		int f = (int) mContext.getResources().getDimension(
				R.dimen.activity_vertical_margin);
		mLBodyView.getLayoutParams().width = screenW * 2 - 2 * f;
		mLlFirstView.getLayoutParams().width = screenW - 2 * f;
		mLlSecondView.getLayoutParams().width = screenW - 2 * f;
		offset = (screenW - 2 * f) / 8;
		// add your item
		mLlFirstView.addView(itemFrist);
		mLlSecondView.addView(itemSecond);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void scrollToRight() {
		mScrollView.post(new Runnable() {
			public void run() {
				mScrollView.smoothScrollTo(0, mY);
				isMove = false;
			}
		});
	}

	public void scrollToLeft() {
		mScrollView.post(new Runnable() {
			public void run() {
				mScrollView.smoothScrollTo(10000, mY);
				isMove = true;
			}
		});
	}

	public void scrollBack() {
		scrollToRight();
	}

	private SwipeDetectorCallBack mSwipeDetectorCallBack;

	interface SwipeDetectorCallBack {
		void onToRight();

		void onBackRight();

		void onToLeft();

		void onBackLeft();
	}

	class SwipeDetector extends SimpleOnGestureListener {
		SwipeDetectorCallBack swipeDetectorCallBack;

		public SwipeDetector(SwipeDetectorCallBack swipeDetectorCallBack) {
			this.swipeDetectorCallBack = swipeDetectorCallBack;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (mRestCb != null) {
				mRestCb.run();
			}
			
			try {
				if (e1.getX() - e2.getX() > offset) {
					swipeDetectorCallBack.onToLeft();
				} else if (e2.getX() - e1.getX() > offset) {
					swipeDetectorCallBack.onToRight();
				} else if (e1.getX() - e2.getX() < offset
						&& e1.getX() - e2.getX() > 0) {
					swipeDetectorCallBack.onBackRight();
				} else if (e1.getX() - e2.getX() > offset * -1
						&& e1.getX() - e2.getX() < 0) {
					swipeDetectorCallBack.onBackLeft();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mRestCb != null) {
				mRestCb.run();
			}
			if (!isMove) {
				scrollToLeft();
			}
			return super.onSingleTapConfirmed(e);
		}
	}

}
