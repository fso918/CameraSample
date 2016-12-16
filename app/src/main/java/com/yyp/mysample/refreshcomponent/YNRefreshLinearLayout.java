/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 可以下拉刷新的LinearLayout，默认的RecyclerView会在滑动到底部时自动加载更多。
 * 此控件处理顶部的刷新控件显示和隐藏的动画，以及刷新数据动作的回调
 * Create by yanyunpeng
 * Date: 2016/10/27 11:43
 */

public class YNRefreshLinearLayout extends LinearLayout {
    private View mTarget;                           //绑定数据的View
    private YNRefreshAnimView refreshAnimView;      //顶部刷新的控件
    private LinearLayout refreshHeader;             //顶部刷新控件的父view

    private Context context;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;
    private float mTotalDragDistance = -1;

    private int mActivePointerId = -1;
    private boolean mIsBeingDragged = false;
    private float mInitialDownY;
    private int mTouchSlop;
    private float mInitialMotionY;

    private OnRefreshListen mRefreshListen;
    private DecelerateInterpolator mDecelerateInterpolator;

    private boolean canRefreshingDraw;                      //正在刷新时是否可以下拉页面
    private boolean isRefreshing;                           //是否正在刷新数据，可以设置在刷新数据的时候页面数据是否可以下拉
    private boolean isPerformingAnim = false;              //是否正在进行动画，返回到刷新状态或原始状态的动画，在此状态不处理onTouch事件

    public YNRefreshLinearLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public YNRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
//        refreshAnimView = LayoutInflater.from(context).inflate(R.layout.widget_refresh_view_header, null);
        setOrientation(VERTICAL);
        mTotalDragDistance = dip2px(context, 80) + 2;
        refreshHeader = new LinearLayout(context);
//        refreshHeader = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.widget_refresh_view_header, null);
        addView(refreshHeader, LayoutParams.MATCH_PARENT, (int)mTotalDragDistance);
        mTarget = new YNRecyclerView(context);
        addView(mTarget, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(0, (int)(-mTotalDragDistance), 0, 0);

        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
    }

    public float dip2px(Context context, float dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return  (dip * scale + 0.5f);
    }
    boolean mReturningToStart;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
//                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mIsBeingDragged) {
//                    mProgress.showArrow(true);
                    float originalDragPercent = overscrollTop / mTotalDragDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }
                    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
                    if(refreshAnimView != null){
                        refreshAnimView.onDraging(adjustedPercent);
                    }
//                    Log.i("TEST_YN", "dragPercent--->>>" + adjustedPercent);

//                    float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;

//                    LayoutParams lp = (LinearLayout.LayoutParams)mTarget.getLayoutParams();
//                    lp.topMargin = (int)overscrollTop;
//                    mTarget.setLayoutParams(lp);
                    int paddingTop = Float.valueOf(-mTotalDragDistance + overscrollTop).intValue();
                    setPadding(0, paddingTop, 0, 0);
                    if(refreshAnimView != null) {
                        if (paddingTop >= 0) {
                            refreshAnimView.onDragingStatusChanged(YNRefreshAnimView.DRAGING_RELEASE_REFRESH);
                        } else {
                            refreshAnimView.onDragingStatusChanged(YNRefreshAnimView.DRAGING);
                        }
                    }

//                    Log.i("TEST_YN", "#############" + overscrollTop);
//                    float slingshotDist = mUsingCustomStart ? mSpinnerFinalOffset
//                            - mOriginalOffsetTop : mSpinnerFinalOffset;
//                    float tensionSlingshotPercent = Math.max(0,
//                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
//                    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
//                            (tensionSlingshotPercent / 4), 2)) * 2f;
//                    float extraMove = (slingshotDist) * tensionPercent * 2;
//
//                    int targetY = mOriginalOffsetTop
//                            + (int) ((slingshotDist * dragPercent) + extraMove);
//                    // where 1.0f is a full circle
//                    if (mCircleView.getVisibility() != View.VISIBLE) {
//                        mCircleView.setVisibility(View.VISIBLE);
//                    }
//                    if (!mScale) {
//                        ViewCompat.setScaleX(mCircleView, 1f);
//                        ViewCompat.setScaleY(mCircleView, 1f);
//                    }
//                    if (overscrollTop < mTotalDragDistance) {
//                        if (mScale) {
//                            setAnimationProgress(overscrollTop / mTotalDragDistance);
//                        }
//                        if (mProgress.getAlpha() > STARTING_PROGRESS_ALPHA
//                                && !isAnimationRunning(mAlphaStartAnimation)) {
//                            // Animate the alpha
//                            startProgressAlphaStartAnimation();
//                        }
//                        float strokeStart = adjustedPercent * .8f;
//                        mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));
//                        mProgress.setArrowScale(Math.min(1f, adjustedPercent));
//                    } else {
//                        if (mProgress.getAlpha() < MAX_ALPHA
//                                && !isAnimationRunning(mAlphaMaxAnimation)) {
//                            // Animate the alpha
//                            startProgressAlphaMaxAnimation();
//                        }
//                    }
//                    float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
//                    mProgress.setProgressRotation(rotation);
//                    setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop,
//                            true /* requires update */);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
//                        Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overscrollTop >= mTotalDragDistance) {
//                    setRefreshing(true, true /* notify */);
                    startAnimToStartOffset(overscrollTop);
                } else {
                    startAnimToOriginOffset(overscrollTop);
                    // cancel refresh
//                    mRefreshing = false;
//                    mProgress.setStartEndTrim(0f, 0f);
//                    Animation.AnimationListener listener = null;
//                    if (!mScale) {
//                        listener = new Animation.AnimationListener() {
//
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                if (!mScale) {
//                                    startScaleDownAnimation(null);
//                                }
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//                            }
//
//                        };
//                    }
//                    animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
//                    mProgress.showArrow(false);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    /**
     * 将顶部的刷新View返回到最初的状态（隐藏）
     * @param overScrollTop
     */
    private void startAnimToOriginOffset(float overScrollTop){
        int paddingTop = Float.valueOf(-mTotalDragDistance + overScrollTop).intValue();
        ValueAnimator animator = ValueAnimator.ofFloat(paddingTop, -mTotalDragDistance);
//        Log.i("TEST_YN", "paddingTop===>>>" + paddingTop + ",---->" + -mTotalDragDistance);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTopTmp = ((Float) animation.getAnimatedValue()).intValue();
                setPadding(0, paddingTopTmp, 0, 0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isPerformingAnim = true;
                isRefreshing = false;
                if(refreshAnimView != null){
                    refreshAnimView.onDragingStatusChanged(YNRefreshAnimView.FINISH);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPerformingAnim = false;
                if(refreshAnimView != null){
                    refreshAnimView.stopLoadingAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isPerformingAnim = false;
                if(refreshAnimView != null){
                    refreshAnimView.stopLoadingAnim();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 将顶部的刷新View返回到开始状态（刷新的状态）
     * @param overScrollTop
     */
    private void startAnimToStartOffset(float overScrollTop){
        int paddingTop = Float.valueOf(-mTotalDragDistance + overScrollTop).intValue();
        ValueAnimator animator = ValueAnimator.ofFloat(paddingTop, 0);
        animator.setDuration(300);
        animator.setInterpolator(mDecelerateInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = ((Float) animation.getAnimatedValue()).intValue();
                setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isPerformingAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPerformingAnim = false;
                if(mRefreshListen != null){
                    isRefreshing = true;
                    mRefreshListen.onRefresh();
                }
                if(refreshAnimView != null){
                    refreshAnimView.onDragingStatusChanged(YNRefreshAnimView.REFRESHING);
                    refreshAnimView.startLoadingAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isPerformingAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(canChildScrollUp()                                   //子View能够往上滚动，此时由子控件处理滚动
                || isPerformingAnim                             //正在进行动画
                || (!canRefreshingDraw && isRefreshing)){       //页面停留在刷新状态，但设置刷新时不可以下拉页面
            return false;
        }
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == INVALID_POINTER) {
                    return false;
                }
                mInitialDownY = initialDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == INVALID_POINTER) {
                    return false;
                }
                final float yDiff = y - mInitialDownY;
                if (yDiff > (float)mTouchSlop && !mIsBeingDragged) {
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                    mIsBeingDragged = true;
//                    Log.i("TEST_YN", "intercept touch=========");
//                    mProgress.setAlpha(STARTING_PROGRESS_ALPHA);
                }
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = -1;
                break;
        }
        return mIsBeingDragged;
    }

    /**
     * 显示刷新控件
     */
    public void showRefresh(){
        setPadding(0, 0, 0, 0);
        if(refreshAnimView != null){
            refreshAnimView.startLoadingAnim();
        }
    }

    /**
     * 隐藏刷新控件，会走一个位移动画
     */
    public void hideRefresh(){
        startAnimToOriginOffset(mTotalDragDistance);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if(pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0?1:0;
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }

    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        return index < 0?-1.0F:MotionEventCompat.getY(ev, index);
    }

    /**
     * 子控件能否向上滚动（没到顶）
     * @return
     */
    public boolean canChildScrollUp() {
        if(Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTarget, -1);
        } else if(this.mTarget instanceof AbsListView) {
            AbsListView absListView = (AbsListView)this.mTarget;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return ViewCompat.canScrollVertically(this.mTarget, -1) || this.mTarget.getScrollY() > 0;
        }
    }

    public View getRefreshView() {
        return mTarget;
    }

    public void setRefreshListen(OnRefreshListen mRefreshListen) {
        this.mRefreshListen = mRefreshListen;
    }

    public void setRefreshAnimView(YNRefreshAnimView refreshView) {
        this.refreshAnimView = refreshView;
        refreshHeader.addView(refreshAnimView, LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTotalDragDistance = refreshView.getTotalHeight();
    }

    /**
     * 添加头部
     * @param view
     */
    public void setHeader(View view){
        if(mTarget != null){
            if(mTarget instanceof ListView){
                ((ListView) mTarget).addHeaderView(view);
            }else if(mTarget instanceof YNRecyclerView){
                ((YNRecyclerView) mTarget).setHeaderView(view);
            }
        }
    }

    public void setCanRefreshingDraw(boolean canRefreshingDraw) {
        this.canRefreshingDraw = canRefreshingDraw;
    }

    public void setHeaderRes(int resId){
        if(mTarget instanceof YNRecyclerView){
            ((YNRecyclerView) mTarget).setHeaderRes(resId);
        }
    }
    interface OnRefreshListen{
        void onRefresh();
    }
}
