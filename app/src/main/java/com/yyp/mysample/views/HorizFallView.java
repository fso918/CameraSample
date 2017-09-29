package com.yyp.mysample.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by fso91 on 2017/9/29.
 */

public class HorizFallView extends ViewGroup {
    private Context context;
    private FallViewDataSetObserver dataSetObserver;
    private BaseAdapter adapter;
    private boolean[] newLineFlag;
    private int widthSpace = 20;
    private int heightSpace = 20;

    public HorizFallView(Context context) {
        super(context);
        init(context);
    }

    public HorizFallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizFallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
    }

    public void setAdapter(BaseAdapter adapter){
        if(this.adapter != null && dataSetObserver != null){
            this.adapter.unregisterDataSetObserver(dataSetObserver);
            dataSetObserver = null;
            removeAllViewsInLayout();
        }
        this.adapter = adapter;
        if(adapter != null && dataSetObserver == null) {
            dataSetObserver = new FallViewDataSetObserver();
            adapter.registerDataSetObserver(dataSetObserver);
        }
        int count  = adapter == null ? 0 : adapter.getCount();
        newLineFlag = new boolean[count];
        for(int i = 0; i < count; i++){
            addView(adapter.getView(i, null, this));
            if(i == 0){
                newLineFlag[i] = true;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //支持Wrapper_content
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int measureWidth = 0, measureHeight = 0;
        int measureWidthTmp = 0, measureHeightTmp = 0;
        int count = getChildCount();
        View child = null;
        LayoutParams lp = null;
        for(int i = 0; i < count; i++) {
            child = getChildAt(i);
            lp = child.getLayoutParams();
            child.measure(getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft()
                            + this.getPaddingRight(), lp.width),
                    getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop()
                            + this.getPaddingBottom(), lp.height));
            //处理宽度
            if((measureWidthTmp + child.getMeasuredWidth()) < widthSpec){
                measureWidthTmp = measureWidthTmp + child.getMeasuredWidth() + widthSpace;
                if(measureWidthTmp > measureWidth){
                    measureWidth = measureWidthTmp;
                }
                if(measureWidth > widthSpec){
                    if(widthSpecMode != MeasureSpec.UNSPECIFIED){
                        measureWidth = widthSpec;
                    }
                }
            } else {
                newLineFlag[i] = true;
                measureWidthTmp = child.getMeasuredWidth() + widthSpace;
                measureHeightTmp = child.getMeasuredHeight() + heightSpace;
            }
            //处理高度
            if(measureHeightTmp < child.getMeasuredHeight() + heightSpace){
                measureHeightTmp = child.getMeasuredHeight() + heightSpace;
            }
            if(newLineFlag[i]){
                measureHeight += measureHeightTmp;
                if(measureHeight > heightSpec){
                    if(heightSpecMode != MeasureSpec.UNSPECIFIED){
                        measureHeight = heightSpec;
                    }
                }
            }
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(measureWidth, widthSpecMode),
                MeasureSpec.makeMeasureSpec(measureHeight, heightSpecMode));
    }

    @Override
    protected void onLayout(boolean change, int left, int top, int right, int bottom) {
        int count = getChildCount();
        View child = null;
        int curLeft = 0;
        int curTop = 0;
        int nextLeft = getPaddingLeft() + widthSpace / 2;
        int nextTop = getPaddingTop() + heightSpace / 2;
        for(int i = 0; i < count; i++){
            child = getChildAt(i);
            if(newLineFlag[i]) {
                curLeft = getPaddingLeft() + widthSpace / 2;
                curTop = nextTop;
                nextTop = curTop + child.getMeasuredHeight() + heightSpace;
            } else {
                curLeft = nextLeft;
                if(curTop + child.getMeasuredHeight() + heightSpace > nextTop){
                    nextTop = curTop + child.getMeasuredHeight() + heightSpace;
                }
            }
            nextLeft = curLeft + child.getMeasuredWidth() + widthSpace;
            child.layout(curLeft, curTop, curLeft + child.getMeasuredWidth(), curTop + child.getMeasuredHeight());
        }
    }

    public class FallViewDataSetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }
    }
}
