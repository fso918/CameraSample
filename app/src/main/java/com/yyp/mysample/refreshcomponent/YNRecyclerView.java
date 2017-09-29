/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用于显示列表数据，滚动的底部会自动加载更多（如果可以加载更多的话）
 * 以及回调滚动方向改变的事件
 * Create by yanyunpeng
 * Date: 2016/11/11 15:42
 */
public class YNRecyclerView extends RecyclerView {
	/** 顶部空隙 **/
	private int topSpace;
	/** true表示手指从上移到下，列表向上滚动，应该显示底部和顶部条，false相反 **/
	private boolean scrollUp = true;
	/** 缓慢滚动时是否暂停加载数据 **/
//	private boolean pauseOnDragging = true;
//	/** 快速滑动时是否暂停加载数据 **/
//	private boolean pauseOnSettling = true;
	private BaseRecyclerViewAdapter adapter;
	private MultiStateLayout multiStateLayout;		//用于显示其他状态，入无数据，网络错误等
	private OnScrollDirectionChangedListener onScrollDirectionChangedListener;			//滚动方向改变的监听
	private View headerView;						//头部的View
	private int headerViewResId = -1;

	//当有数据变化时，判断显示状态是显示EmptyView还是contentView
	private final AdapterDataObserver observer = new AdapterDataObserver() {
		@Override
		public void onChanged() {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			checkIfEmpty();
		}
	};

	public YNRecyclerView(Context context) {
		this(context, null);
	}

	public YNRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YNRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				/*
				* 滚动方向改变监听
				* dy=控件滚动前的位置-控件滚动后的位置，dy>0,表示滚动前Y坐标大于滚动后Y坐标，滚动后控件更加靠近
				* 屏幕顶部，所以此时列表内容向底部滚动，dy<0相反.
				*/
				if (onScrollDirectionChangedListener != null && ((dy > 0 && scrollUp) || (dy < 0 && !scrollUp))) {
					scrollUp = !scrollUp;
					onScrollDirectionChangedListener.onScrollDirectionChanged(scrollUp);
				}
				// 滚动到底部自动加载更多
				final LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
				if (dy > 0 && adapter != null && adapter.canLoadingMore() && layoutManager != null
						&& layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1) { // 剩下1个item自动加载
					adapter.startLoadMore();
				}

				super.onScrolled(recyclerView, dx, dy);
			}

		});
	}

	/**
	 * 设置滚动方向改变监听器
	 * @param onScrollDirectionChangedListener
	 */
	public void setOnScrollDirectionChangedListener(OnScrollDirectionChangedListener onScrollDirectionChangedListener) {
		this.onScrollDirectionChangedListener = onScrollDirectionChangedListener;
	}

	/**
	 * 设置顶部空隙
	 * @param topSpace
	 */
	public void setTopSpace(int topSpace) {
		this.topSpace = topSpace;
		Adapter adapter = getAdapter();
		if (adapter != null && adapter instanceof BaseRecyclerViewAdapter) {
			((BaseRecyclerViewAdapter) adapter).setTopSpace(topSpace);
		}
	}

	@Override
	public void setAdapter(Adapter adapter) {
		final Adapter oldAdapter = getAdapter();
		if (oldAdapter != null) {
			oldAdapter.unregisterAdapterDataObserver(observer);
		}
		if (adapter != null) {
			super.setAdapter(adapter);
			adapter.registerAdapterDataObserver(observer);
		}

		if (adapter != null && adapter instanceof BaseRecyclerViewAdapter) {
			((BaseRecyclerViewAdapter) adapter).setTopSpace(topSpace);
			this.adapter = (BaseRecyclerViewAdapter) adapter;
			if(headerView != null){
				this.adapter.setHeaderView(headerView);
			}else if(headerViewResId != -1){
				this.adapter.setHeaderResource(headerViewResId);
			}
		}
	}

	public void checkIfEmpty() {
		if (multiStateLayout != null && adapter != null) {
			if (adapter.getItemNum() == 0) {
				multiStateLayout.showEmpty();
			} else {
				multiStateLayout.showContent();
			}
		}
	}

	public PageRecyclerLayout getRefreshLayout() {
		if (multiStateLayout != null && multiStateLayout instanceof PageRecyclerLayout) {
			return (PageRecyclerLayout) multiStateLayout;
		} else {
			return null;
		}
	}

	public void setMultiStateLayout(MultiStateLayout refreshLayout) {
		this.multiStateLayout = refreshLayout;
	}

	/**
	 * 添加头部
	 * @param view
     */
	public void setHeaderView(View view){
		if(adapter != null) {
			adapter.setHeaderView(view);
			headerView = null;
		}else{
			headerView = view;
		}
	}

	public void setHeaderRes(int resId){
		if(adapter != null) {
			adapter.setHeaderResource(resId);
			headerViewResId = -1;
		}else {
			headerViewResId = resId;
		}
	}


	/**
	 * 滚动方向改变监听器
	 */
	public interface OnScrollDirectionChangedListener {
		/**
		 * 滚动方向改变
		 * @param scrollUp true表示手指从上移到下，列表向上滚动，应该显示底部和顶部条，false相反
		 */
		void onScrollDirectionChanged(boolean scrollUp);
	}
}
