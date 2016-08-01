package com.yyp.mysample.feature.camera.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yyp.mysample.utils.DisplayUtils;

import java.lang.reflect.Field;


/**
 * <description>
 * Create by yanyunpeng
 * Date: 2016/7/27 14:31
 */
public class RectCameraMaskView extends ImageView {
    private int screenWidth;
    private int screenHeight;

    private Paint dimPaint;         //暗色画笔
    private Paint linePaint;        //线条画笔

    private float widthRatio;       //中间明亮区域占的宽度比率
    private float heightRatio;      //中间亮框占的高度比
    private float aspectRatio;

    private int lineWeight;         //边框线粗细
    private int halfLineWeight;     //边框线粗细的一半
    private int lineLength;         //边框线长度
    private int lineColor;          //边框线颜色
    private int lineAlpha;          //边框线透明度
    private int shadowAlpha;        //阴影区域透明度

    private int cutTop;             //上部不显示照相机预览内容的高度，比如标题高度
    private int cutLeft;            //左边不显示照相机预览内容的高度
    private int cutRight;           //右边不显示照相机预览内容的高度
    private int cutBottom;          //底部不显示照相机预览内容的高度，比如底部栏高度
    private int statusHeight;       //状态栏高度

    private int left = -1;
    private int top = -1;

    private Context context;

    /**
     * 中间亮框的宽高比是否适配屏幕宽高比,如果为true,则亮框的宽高比和屏幕的宽高比一致，
     * 设置的高度比率heightRatio将不起作用
     */
    private boolean isAdapteScreen;

    private DrawParameters drawParameters;

    public RectCameraMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        screenHeight = DisplayUtils.getScreenHeight(context);
        screenWidth = DisplayUtils.getScreenWith(context);

        statusHeight = DisplayUtils.getStatusHeight(context);
        aspectRatio = screenWidth / (screenHeight + 0f);

        setDrawParameters(new DrawParameters());
        initPaint();
    }

    /**
     * 初始化相关画笔
     */
    private void initPaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWeight);
        linePaint.setAlpha(lineAlpha);

        dimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dimPaint.setColor(Color.BLACK);
        dimPaint.setStyle(Paint.Style.FILL);
        dimPaint.setAlpha(shadowAlpha);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (left == -1 || top == -1) {
            left = Float.valueOf((screenWidth - cutLeft - cutRight - (screenWidth - cutLeft - cutRight) * widthRatio) / 2).intValue();
            if (isAdapteScreen) {
                top = Float.valueOf((screenHeight - cutTop - cutBottom - statusHeight - screenWidth * widthRatio / aspectRatio) / 2).intValue();
            } else {
                int visibleHeight = screenHeight - statusHeight - cutTop - cutBottom;
                top = Float.valueOf((visibleHeight - visibleHeight * heightRatio) / 2).intValue();
            }
        }

        canvas.drawRect(cutLeft, cutTop,
                screenWidth - cutRight,
                top + cutTop,
                dimPaint);
        canvas.drawRect(cutLeft,
                screenHeight - top - cutBottom - statusHeight,
                screenWidth - cutRight,
                screenHeight - cutBottom - statusHeight,
                dimPaint);
        canvas.drawRect(cutLeft,
                top + cutTop,
                left + cutLeft,
                screenHeight - top - cutBottom - statusHeight,
                dimPaint);
        canvas.drawRect(screenWidth - left - cutRight,
                top + cutTop,
                screenWidth - cutRight,
                screenHeight - top - cutBottom - statusHeight,
                dimPaint);

        //左上角边框线
        canvas.drawLine(left - lineWeight,
                top - halfLineWeight + cutTop,
                left + lineLength - lineWeight,
                top - halfLineWeight + cutTop,
                linePaint);
        canvas.drawLine(left - halfLineWeight,
                top + cutTop,
                left - halfLineWeight,
                top + lineLength + cutTop - lineWeight,
                linePaint);

        //右上角边框线
        canvas.drawLine(screenWidth - left - cutRight - lineLength + lineWeight,
                top - halfLineWeight + cutTop,
                screenWidth - cutRight - left + lineWeight,
                top - halfLineWeight + cutTop,
                linePaint);
        canvas.drawLine(screenWidth - cutRight - left + halfLineWeight,
                top + cutTop,
                screenWidth - cutRight - left + halfLineWeight,
                top + lineLength + cutTop - lineWeight,
                linePaint);

        //左下边框线
        canvas.drawLine(cutLeft + left - lineWeight,
                screenHeight - cutBottom - top - statusHeight + halfLineWeight,
                cutLeft + left + lineLength - lineWeight,
                screenHeight - cutBottom - top - statusHeight + halfLineWeight,
                linePaint);
        canvas.drawLine(cutLeft + left - halfLineWeight,
                screenHeight - cutBottom - top - statusHeight - lineLength + lineWeight,
                cutLeft + left - halfLineWeight,
                screenHeight - cutBottom - top - statusHeight,
                linePaint);

        //右下边框线
        canvas.drawLine(screenWidth - cutRight - left - lineLength + lineWeight,
                screenHeight - cutBottom - top - statusHeight + halfLineWeight,
                screenWidth - cutRight - left + lineWeight,
                screenHeight - cutBottom - top - statusHeight + halfLineWeight,
                linePaint);
        canvas.drawLine(screenWidth - cutRight - left + halfLineWeight,
                screenHeight - cutBottom - top - statusHeight - lineLength + lineWeight,
                screenWidth - cutRight - left + halfLineWeight,
                screenHeight - cutBottom - top - statusHeight,
                linePaint);
    }

    public DrawParameters getDrawParameters() {
        return drawParameters;
    }

    public void setDrawParameters(DrawParameters params) {
        this.widthRatio = params.widthRatio;
        this.heightRatio = params.heightRatio;
        this.lineWeight = params.lineWeight;
        this.halfLineWeight = params.lineWeight / 2;
        this.lineLength = params.lineLength;
        this.lineColor = params.lineColor;
        if (linePaint != null) {
            linePaint.setColor(lineColor);
        }
        this.lineAlpha = params.lineAlpha;
        this.shadowAlpha = params.shadowAlpha;

        this.cutLeft = params.cutLeft;
        this.cutTop = params.cutTop;
        this.cutRight = params.cutRight;
        this.cutBottom = params.cutBottom;

        this.isAdapteScreen = params.isAdapterScreen;

        this.drawParameters = params;
    }

    public int getShadowTop() {
        return top;
    }

    public int getShadowLeft() {
        return left;
    }

    private Context getViewContext(){
        return context;
    }

    /**
     * 绘制时的参数
     */
    public class DrawParameters {
        private float widthRatio = 0.6f;
        private float heightRatio = 0.7f;

        private int lineWeight = DisplayUtils.dip2px(context, 3);
        private int lineLength = DisplayUtils.dip2px(context, 15);
        private int lineColor = Color.BLUE;
        private int lineAlpha = 90;
        private int shadowAlpha = 80;

        private int cutTop;
        private int cutLeft;
        private int cutRight;
        private int cutBottom;

        private boolean isAdapterScreen = false;

        private DrawParameters() {
        }

        public float getWidthRatio() {
            return widthRatio;
        }

        public void setWidthRatio(float widthRatio) {
            if (widthRatio <= 0 || Math.abs(widthRatio - 1) > 0) return;
            this.widthRatio = widthRatio;
        }

        public float getHeightRatio() {
            return heightRatio;
        }

        public void setHeightRatio(float heightRatio) {
            if (heightRatio <= 0 || Math.abs(heightRatio - 1) > 0) return;
            this.heightRatio = heightRatio;
            this.isAdapterScreen = false;
        }

        public boolean isAdapterScreen() {
            return isAdapterScreen;
        }

        public void setAdapterScreen(boolean adapterScreen) {
            isAdapterScreen = adapterScreen;
            if (adapterScreen) {
                heightRatio = 0.7f;
            }
        }

        public int getLineWeight() {
            return lineWeight;
        }

        public void setLineWeight(int lineWeight) {
            if (lineWeight <= 0) lineWeight = 0;
            this.lineWeight = lineWeight;
        }

        public int getLineLength() {
            return lineLength;
        }

        public void setLineLength(int lineLength) {
            if (lineLength <= 0) lineLength = 0;
            this.lineLength = lineLength;
        }

        public int getLineColor() {
            return lineColor;
        }

        public void setLineColor(int lineColor) {
            this.lineColor = lineColor;
        }

        public int getLineAlpha() {
            return lineAlpha;
        }

        public void setLineAlpha(int lineAlpha) {
            this.lineAlpha = lineAlpha;
        }

        public int getCutTop() {
            return cutTop;
        }

        public void setCutTop(int cutTop) {
            if (cutTop < 0) cutTop = 0;
            this.cutTop = cutTop;
        }

        public int getCutLeft() {
            return cutLeft;
        }

        public void setCutLeft(int cutLeft) {
            if (cutLeft < 0) cutLeft = 0;
            this.cutLeft = cutLeft;
        }

        public int getCutRight() {
            return cutRight;
        }

        public void setCutRight(int cutRight) {
            if (cutRight < 0) cutRight = 0;
            this.cutRight = cutRight;
        }

        public int getCutBottom() {
            return cutBottom;
        }

        public void setCutBottom(int cutBottom) {
            if (cutBottom < 0) cutBottom = 0;
            this.cutBottom = cutBottom;
        }

        public int getShadowAlpha() {
            return shadowAlpha;
        }

        public void setShadowAlpha(int shadowAlpha) {
            this.shadowAlpha = shadowAlpha;
        }
    }
}
