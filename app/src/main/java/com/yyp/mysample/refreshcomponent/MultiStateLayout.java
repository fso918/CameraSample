/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yyp.mysample.R;

/**
 * 多状态FrameLayout
 * 四种状态：
 *  1、正常状态，显示内容；
 *  2、空状态，显示EmptyView,
 *  3、错误状态，显示ErrorView,
 *  4、加载状态，显示加载动画（不是列表的刷新动画，列表的刷新由SwiftRefreshView控制）
 * Create by yanyunpeng
 * Date: 2016/10/20 16:41
 */
public class MultiStateLayout extends FrameLayout {
	protected Context context;

	/** 正常状态，隐藏进度条，错误和空View，显示内容 **/
	public static final int STATE_NORMAL = 0;
	/** 正在加载中，显示进度条，隐藏错误和空View,内容是否可见可以设置 **/
	public static final int STATE_LOADING = 1;
	/** 显示空View,隐藏其他View **/
	public static final int STATE_EMPTY = 2;
	/** 显示错误View,隐藏其他View **/
	public static final int STATE_ERROR = 3;
	
	protected View contentView;
	protected ViewGroup emptyView;
	protected ViewGroup errorView;
	private ViewGroup progressbar;
	private int emptyResourceId = -1;
	private int errorResourceId = -1;
	private String emptyText;
	private String errorText;
	/** 顶部空隙 **/
	protected int topSpace;
	/** 底部空隙 **/
	protected int bottomSpace;
	/** 如果是xml布局中使用，该参数指明xml布局中child的数目（已经保证了0到xmlChildCount内的view不包含空view、错误view、加载中view）**/
	private int xmlChildCount;
	/** xml布局是否加载完毕 **/
	private boolean finishInflate;
	/** 当前显示状态 **/
	private int state = STATE_NORMAL;
	private boolean contentVisibilityOnLoading = true;

	private Drawable emptyDrawableTop;
	private Drawable errorDrawableTop;
	private boolean showDrawable = true;		//错误页面，空页面是否显示textview的drawable

	public MultiStateLayout(Context context) {
		super(context);
		this.context = context;
	}

	public MultiStateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MultiStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		finishInflate = true;
		if (contentView == null) {// 设置
			xmlChildCount = getChildCount();
		}
		updateState();
	}

	/**
	 * 设置显示内容
	 * @param contentView
	 */
	public void setContentView(View contentView) {
		this.contentView = contentView;
		if(contentView.getParent() == null) {
			addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			finishInflate = true;
			updateState();
		}else{
			throw new RuntimeException("添加的View已经有父控件了。");
		}
	}
	
	public void setEmptyView(ViewGroup emptyView) {
		this.emptyView = emptyView;
		if (emptyView.getParent() == null) {
			addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			throw new RuntimeException("添加的View已经有父控件了。");
		}
	}

	/**
	 * 根据状态更新当前显示
	 **/
	public void updateState() {
		if (!finishInflate) {
			return;
		}
		switch (state) {
			case STATE_LOADING:		//加载
				showProgressView();
				hideEmptyView();
				hideErrorView();
				if(contentVisibilityOnLoading){
					showContentView();
				}else {
					hideContentView();
				}
				break;
			case STATE_NORMAL:
				hideProgressView();
				showContentView();
				hideEmptyView();
				hideErrorView();
				break;
			case STATE_EMPTY:
				hideProgressView();
				hideContentView();
				showEmptyView();
				hideErrorView();
				break;
			case STATE_ERROR:
				hideProgressView();
				hideContentView();
				hideEmptyView();
				showErrorView();
				break;
			default:
				break;
		}
	}

	/**
	 * 显示加载
	 */
	public void showLoading() {
		this.state = STATE_LOADING;
		updateState();
	}

	/**
	 * 显示内容
	 */
	public void showContent() {
		this.state = STATE_NORMAL;
		updateState();
	}

	/**
	 * 显示空View
	 */
	public void showEmpty() {
		this.state = STATE_EMPTY;
		updateState();
	}

	/**
	 * 显示错误
	 */
	public void showError() {
		this.state = STATE_ERROR;
		updateState();
	}

	public View getContentView() {
		return contentView;
	}

	private void showContentView() {
		if (contentView != null) {
			contentView.setVisibility(View.VISIBLE);
		} else if (xmlChildCount != 0) {
			int count = xmlChildCount;
			for (int i = 0; i < count; ++i) {
				getChildAt(i).setVisibility(View.VISIBLE);
			}
		}
	}

	private void hideContentView() {
		if (contentView != null) {
			contentView.setVisibility(View.GONE);
		} else if (xmlChildCount != 0) {
			int count = xmlChildCount;
			for (int i = 0; i < count; ++i) {
				getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	private void showProgressView() {
		if (progressbar == null) {
			progressbar = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.widget_loading_view, null);
			if (topSpace > 0 || bottomSpace > 0) {
				progressbar.setPadding(0, progressbar.getPaddingTop() + topSpace, 0, progressbar.getPaddingBottom() + bottomSpace);
			}
			addView(progressbar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			progressbar.setVisibility(View.VISIBLE);
		}
	}

	private void hideProgressView() {
		if (progressbar != null) {
			progressbar.setVisibility(View.GONE);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
	}

	private void showEmptyView() {
		if (emptyView == null) {
			emptyView = (ViewGroup) LayoutInflater.from(getContext()).inflate(emptyResourceId == -1 ? R.layout.widget_empty_view : emptyResourceId, null);
			if (topSpace > 0 || bottomSpace > 0) {
				emptyView.setPadding(0, emptyView.getPaddingTop() + topSpace, 0, emptyView.getPaddingBottom() + bottomSpace);
			}
			TextView textview = (TextView) emptyView.findViewById(R.id.empty_tv);
			if (textview != null) {
				if (!TextUtils.isEmpty(emptyText)) {
					textview.setText(emptyText);
				}
				if(showDrawable){
					if(emptyDrawableTop == null){
						emptyDrawableTop = context.getResources().getDrawable(R.mipmap.no_data);
					}
					emptyDrawableTop.setBounds(0, 0, emptyDrawableTop.getMinimumWidth(), emptyDrawableTop.getMinimumHeight());
					textview.setCompoundDrawables(null, emptyDrawableTop, null, null);
				}
			}
			initEmptyView(emptyView);
			addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化空view
	 * @param emptyview
	 */
	protected void initEmptyView(ViewGroup emptyview) {
	}

	private void hideEmptyView() {
		if (emptyView != null) {
			emptyView.setVisibility(View.GONE);
		}
	}

	private void showErrorView() {
		if (errorView == null) {
			errorView = (ViewGroup) LayoutInflater.from(getContext()).inflate(errorResourceId == -1 ? R.layout.widget_error_view : errorResourceId, null);
			if (topSpace > 0 || bottomSpace > 0) {
				errorView.setPadding(0, errorView.getPaddingTop() + topSpace, 0, errorView.getPaddingBottom() + bottomSpace);
			}
			TextView textview = (TextView) errorView.findViewById(R.id.error_tv);
			if (textview != null) {
				if (!TextUtils.isEmpty(errorText)) {
					textview.setText(errorText);
				}
				if(showDrawable){
					if(errorDrawableTop == null){
						errorDrawableTop = context.getResources().getDrawable(R.mipmap.pic_load_failed);
					}
					errorDrawableTop.setBounds(0, 0, errorDrawableTop.getMinimumWidth(), errorDrawableTop.getMinimumHeight());
					textview.setCompoundDrawables(null, errorDrawableTop, null, null);
				}
			}
			initErrorView(errorView);
			addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			errorView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 初始化加载失败view
	 * @param
	 */
	protected void initErrorView(ViewGroup errorView) {
	}

	private void hideErrorView() {
		if (errorView != null) {
			errorView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置顶部和底部空隙，使进度条、空view、加载出错view、正常显示内容位置显示正确
	 */
	public void setSpace(int topSpace, int bottomSpace) {
		this.topSpace = topSpace;
		this.bottomSpace = bottomSpace;
	}

	/**
	 * 设置在Loading的时候，内容是否可见
	 * @param visible
     */
	public void setContentVisibilityOnLoading(boolean visible){
		this.contentVisibilityOnLoading = visible;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	public void setEmptyText(int resId) {
		this.emptyText = getContext().getString(resId);
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public void setErrorText(int resId) {
		this.errorText = getContext().getString(resId);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setErrorView(ViewGroup errorView) {
		this.errorView = errorView;
	}

	public void setEmptyLayoutResource(int resourceId) {
		this.emptyResourceId = resourceId;
	}

	public void setErrorLayoutResource(int resourceId) {
		this.errorResourceId = resourceId;
	}

	public boolean isLoading() {
		return state == STATE_LOADING;
	}

	public boolean isNormal() {
		return state == STATE_NORMAL;
	}

	public boolean isEmpty() {
		return state == STATE_EMPTY;
	}

	public boolean isError() {
		return state == STATE_ERROR;
	}

	public Drawable getEmptyDrawableTop() {
		return emptyDrawableTop;
	}

	public void setEmptyDrawableTop(Drawable emptyDrawableTop) {
		this.emptyDrawableTop = emptyDrawableTop;
	}

	public Drawable getErrorDrawableTop() {
		return errorDrawableTop;
	}

	public void setErrorDrawableTop(Drawable errorDrawableTop) {
		this.errorDrawableTop = errorDrawableTop;
	}

	public boolean isShowDrawable() {
		return showDrawable;
	}

	public void setShowDrawable(boolean showDrawable) {
		this.showDrawable = showDrawable;
	}
}