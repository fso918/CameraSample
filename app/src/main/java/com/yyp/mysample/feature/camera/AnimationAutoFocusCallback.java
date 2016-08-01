package com.yyp.mysample.feature.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.yyp.mysample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动对焦功能，及对焦动画
 * Create by yanyunpeng
 * Date: 2016/7/22 11:17
 */
public class AnimationAutoFocusCallback implements AutoFocusCallback {

    private CameraHelper cHelper;
    private Context mContext;
    private AbsoluteLayout focusLayout;
    /* 显示对焦动画的view */
    private View focusView;

    public AnimationAutoFocusCallback(Context context, CameraHelper cHelper, AbsoluteLayout focusLayout) {
        this.mContext = context;
        this.focusLayout = focusLayout;
        this.cHelper = cHelper;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public synchronized void focusOnTouch(MotionEvent event, int focusMode) {
        // 显示开始对焦动画
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            playAutoFocusAnim(event);
        }
        //设置对焦参数
        setFocusParameters(event, focusMode);
    }

    /**
     * 开始对焦动画，动画位置按照触摸点位置计算。
     * Create by yanyunpeng
     * Date: 2016/7/22 14:19
     */
    private void playAutoFocusAnim(MotionEvent event) {
        if (focusLayout == null) return;
        focusView = focusLayout.getChildAt(0);
        if (focusView == null) return;

        AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) focusView.getLayoutParams();
        layoutParams.x = (int) event.getX();
        layoutParams.y = (int) event.getY();
        focusView.setLayoutParams(layoutParams);        //设置动画View的位置
        focusView.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this.mContext, R.anim.anim_auto_focus_scale);
        focusView.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (focusView != null) {
                    focusView.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 设置对焦的参数，包括对焦区域，。。
     */
    private void setFocusParameters(MotionEvent event, int focusMode) {
        if (cHelper == null) return;
        setFocusMode(focusMode);        //设置对焦模式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Camera.Parameters parameters = cHelper.getCameraParameters();
            if (parameters == null) return;
            //计算聚焦区域
            Rect focusRect = calculateTapArea(event.getRawX(), event.getRawY(), 1f);
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
                focusAreas.add(new Camera.Area(focusRect, 1000));
                parameters.setFocusAreas(focusAreas);
            }
            //设置曝光区域
            Rect meteringRect = calculateTapArea(event.getRawX(), event.getRawY(), 1.5f);
            if (parameters.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(meteringRect, 1000));

                parameters.setMeteringAreas(meteringAreas);
            }
            cHelper.getCamera().setParameters(parameters);
            try {
                cHelper.getCamera().autoFocus(this);
            } catch (Exception e) {
//				e.printStackTrace();
                Toast.makeText(mContext, "对焦失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 计算对焦区域
     * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to
     * 1000:1000.
     */
    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        Camera.Size size = getResolution();
        int centerX = (int) (x / size.width - 1000);
        int centerY = (int) (y / size.height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    /**
     * 获取当前的拍照尺寸
     * Create by yanyunpeng
     * Date: 2016/7/22 11:26
     */
    public Camera.Size getResolution() {
        if (cHelper == null || cHelper.getCamera() == null) return null;
        Camera.Parameters params = cHelper.getCamera().getParameters();
        Camera.Size s = params.getPreviewSize();
        return s;
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * 设置自动对焦的对焦方式
     * @param focusMode 对焦方式
     */
    public void setFocusMode(int focusMode) {
        if(cHelper == null || cHelper.getCamera() == null) return;
        Camera.Parameters params = cHelper.getCamera().getParameters();
        if (params == null) return;

        List<String> FocusModes = params.getSupportedFocusModes();
        switch (focusMode) {
            case 0:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                else
                    Toast.makeText(mContext, "Auto Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                else
                    Toast.makeText(mContext, "Continuous Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_EDOF))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
                else
                    Toast.makeText(mContext, "EDOF Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                break;
            case 4:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                else
                    Toast.makeText(mContext, "Infinity Mode not supported", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO))
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                break;
        }

        cHelper.getCamera().setParameters(params);
    }

    @Override
    public void onAutoFocus(boolean arg0, Camera camera) {
        if (cHelper != null && cHelper.getCamera() != null) {
            camera.cancelAutoFocus();
        }
    }

}