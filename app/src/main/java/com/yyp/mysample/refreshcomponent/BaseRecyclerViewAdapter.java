/*
 * Copyright (c) 2013-2014 YunZhongXiaoNiao Tech
 */

package com.yyp.mysample.refreshcomponent;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yyp.mysample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的adapter基类<br/>
 * 管理加载更多和顶部留空隙
 * Create by yanyunpeng
 * Date: 2016/10/21 9:54
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int NORMAL = 0;         //正常的Item内容
    public static final int HEADER = -1;        //头部
    public static final int FOOTER = -2;        //底部，显示加载更多和没有更多数据、加载更多动画

    protected static final int FOOTER_NORMAL = 0;       //底部显示加载更多
    protected static final int FOOTER_LOADING = 1;      //底部显示正在加载
    protected static final int FOOTER_NO_MORE = 2;      //底部显示没有更多内容
    protected static final int FOOTER_HIDE = 3;         //隐藏底部
    protected static final int FOOTER_SPACE = 4;        //底部为空

    protected Context context;
//    protected DriverApplication app;
    protected Resources res;
    protected RecyclerView recyclerView;
    protected LayoutInflater inflater;

    protected View headerView = null;
    protected View footerView = null;
    private ProgressBar footerProgressBar;
    private TextView footerTextView;

    private int topSpace;                   //顶部间隙

    protected List<T> data = new ArrayList<T>();
    protected int footerState;
    private String tag;
    protected int perPageCount = 20;         //默认每页20条数据
    protected int pageNum;                  //页数

    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this(context, recyclerView, true);
    }

    /**
     * @param context
     * @param recyclerView
     * @param spaceFooterHeight 底部空余spaceFooterHeight
     */
    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView, int spaceFooterHeight) {
        this(context, recyclerView, true);
        footerState = FOOTER_SPACE;
        footerProgressBar.setVisibility(View.GONE);
        footerTextView.setVisibility(View.GONE);
        footerView.setMinimumHeight(spaceFooterHeight);
    }

    /**
     * @param context
     * @param recyclerView
     * @param showFooter 是否显示footer
     */
    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView, boolean showFooter) {
        super();
        this.context = context;
//        app = activity.driverApplication;
        res = context.getResources();
        inflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
        if (showFooter) {
            setFooterResource(R.layout.widget_footer);
        }
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        initHeaderView(topSpace);
    }

    public void setHeaderResource(int resId) {
        this.headerView = inflater.inflate(resId, null);
        initHeaderView(topSpace);
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        initFooterView();
    }

    public void setFooterResource(int resId) {
        this.footerView = inflater.inflate(resId, null);
        initFooterView();
    }

    private void initFooterView() {
        footerProgressBar = (ProgressBar) footerView.findViewById(R.id.footer_progressbar);
        footerTextView = (TextView) footerView.findViewById(R.id.footer_textview);
    }

    /**
     * 设置顶部留白
     * @param topSpace
     */
    public void setTopSpace(int topSpace) {
        this.topSpace = topSpace;
        initHeaderView(topSpace);
    }

    private void initHeaderView(int topSpace) {
        if (topSpace > 0) {
            if (headerView == null) {
                headerView = new View(context);
            }
            headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, topSpace));
        }else if(headerView != null){
            headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 是否可以加载更多
     * @return
     */
    public boolean canLoadingMore() {
        return footerView != null && footerState == FOOTER_NORMAL;
    }

    /**
     * 开始加载更多
     */
    synchronized void startLoadMore() {
        if (!canLoadingMore()) {
            return;
        }
        // 改变底部栏状态
        setFooterState(FOOTER_LOADING, true);
        // 请求数据
        ((YNRecyclerView) recyclerView).getRefreshLayout().loadData(pageNum + 1);
    }

    /**
     * 设置底部footer的状态
     * @param footerState
     * @param delayUpdateState 当加载结束footerState变为FOOTER_NORMAL或FOOTER_NO_MORE时，指示是否延迟改变底部栏状态，其它状态时此参数无效
     */
    public void setFooterState(int footerState, boolean delayUpdateState) {
        if (footerView == null || footerState == FOOTER_SPACE) {// 如果没有底部或者底部只是为了留白
            return;
        }
        this.footerState = footerState;
        switch (footerState) {
            case FOOTER_NORMAL:
                // 加载更多完成后新数据显示有个动画时间，在这个时间内底部栏还没消失，延迟底部栏更新状态；如果加载失败，不延迟直接更新状态
                recyclerView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        footerProgressBar.setVisibility(View.GONE);
                        footerTextView.setText("加载更多");
                    }
                }, delayUpdateState ? 2000 : 0);
                break;

            case FOOTER_LOADING:
                footerProgressBar.setVisibility(View.VISIBLE);
                footerTextView.setText("加载中...");
                break;

            case FOOTER_NO_MORE:
                // 加载更多完成后新数据显示有个动画时间，在这个时间内底部栏还没消失，延迟底部栏更新状态；如果加载失败，不延迟直接更新状态
                recyclerView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        footerProgressBar.setVisibility(View.GONE);
                        footerTextView.setText("没有更多数据了");
                    }
                }, delayUpdateState ? 2000 : 0);
                break;

            case FOOTER_HIDE:
                footerView.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public List<T> getDatas() {
        return data;
    }

    public void setData(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }
        this.data = data;
        if (footerView != null) {
            int size = data.size();
            if (size == 0) {                                    //没有数据，隐藏底部
                setFooterState(FOOTER_HIDE, false);
            } else if (data.size() < perPageCount) {            //如果数据比一页的数据少，则表示没有下一页的数据了
                setFooterState(FOOTER_NO_MORE, false);
            }
        }
        pageNum = 1;
        notifyDataSetChanged();
    }

    /**
     * 加载更多添加数据
     * @param data
     */
    public void addData(List<T> data) {
        if (data == null || data.size() == 0) {// 无更多数据了
            setFooterState(FOOTER_NO_MORE, true);
            return;
        }
        int positionStart = getItemCount() - 1;
        this.data.addAll(data);
        setFooterState(data.size() < perPageCount ? FOOTER_NO_MORE : FOOTER_NORMAL, true);
        pageNum++;
        notifyItemRangeInserted(positionStart, data.size());
    }

    /**
     * 子类用getItemNum方法代替
     */
    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (headerView != null)
            headerOrFooter++;
        if (footerView != null && footerState != FOOTER_HIDE)
            headerOrFooter++;
        final int size = getItemNum();
        return size > 0 ? size + headerOrFooter : 0;
    }

    /**
     * 取代getItemCount方法，返回数据的条数
     * @return
     */
    public int getItemNum() {
        return data.size();
    }

    /**
     * 子类用getItemType方法代替
     */
    @Override
    public int getItemViewType(int position) {
        int headerCount = 0;
        if (position == getItemCount() - 1 && footerView != null && footerState != FOOTER_HIDE) {
            return FOOTER;
        } else if (headerView != null) {
            if (position == 0) {
                return HEADER;
            } else {
                headerCount = 1;
            }
        }
        return getItemType(position - headerCount);
    }

    /**
     * 取代getItemViewType方法,子类实现此方法来匹配类型
     * @param position
     * @return
     */
    public int getItemType(int position) {
        return NORMAL;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        protected ProgressBar footer_progressbar;
        protected TextView footer_textview;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footer_progressbar = (ProgressBar) footerView.findViewById(R.id.footer_progressbar);
            footer_textview = (TextView) footerView.findViewById(R.id.footer_textview);
        }
    }

    /**
     * 子类用onBindHolder方法代替
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                break;

            case FOOTER:
                break;

            default:
                // 矫正position
                if (headerView != null )
                    position--;
                onBindItemHolder(holder, position);
                break;
        }
    }

    /**
     * 更新指定位置的数据
     * @param t
     * @param position
     */
    public void notifyItemChanged(T t, int position) {
        if (position < 0) return;
        if(t != null){
            data.set(position, t);
        }
        if (headerView != null)
            position++;
//        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    public void notifyItemRemove(T t, int position) {
        if (position < 0) return;
        data.remove(t);
        if (headerView != null ) {
            position++;
        }
        notifyDataSetChanged();
    }
    public void notifyItemRemove(T t) {
        if(t == null) return;
        int position = data.indexOf(t);
        if (position < 0) return;
        if (headerView != null ) {
            position++;
        }
        data.remove(t);
        notifyDataSetChanged();
    }

    public void notifyInsertTop(T t) {
        data.add(0, t);
        notifyDataSetChanged();
    }

    /**
     * 取代onBindViewHolder方法
     * @param holder
     * @param position
     */
    public abstract void onBindItemHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 子类用onCreateItemHolder方法代替
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderViewHolder(headerView);

            case FOOTER:
                footerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                footerView.setOnClickListener(new onFooterClick());
                return new FooterViewHolder(footerView);

            default:
                return onCreateItemHolder(parent, viewType);
        }
    }

    /**
     * 取代onCreateViewHolder方法
     * @param parent
     * @param viewType
     */
    public abstract RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType);

    /**
     * footer点击监听器
     * @author zhang_ranran
     */
    private final class onFooterClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (footerState == FOOTER_NORMAL) {// 正常情况下，点击可以加载更多
                startLoadMore();
            }
        }
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public BaseRecyclerViewAdapter<T> setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public int getTopSpace() {
        return topSpace;
    }

    public int getPerPageCount() {
        return perPageCount;
    }

    public void setPerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
    }

    public int getPageNum() {
        return pageNum;
    }
}
