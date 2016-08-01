package com.yyp.mysample.feature.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.yyp.mysample.utils.BitmapUtil;
import com.yyp.mysample.utils.DisplayUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 照相机的通用辅助类,支持自动对焦及对焦动画,支持返回数据为bitmap或者图片路径。
 * 暂时不支持前后摄像头切换，不支持视频拍摄。
 * 1、传入的对焦Layout为空，则点击屏幕不会自动对焦，不会执行对焦动画。
 * 2、传入的对焦Layout不为空，但该Layout的子控件为空,则会执行自动对焦，但不会显示对焦动画。
 * 3、如果拍照之后需要对照片的Bitmap进行处理，如裁剪，打水印等，可以实现{@link BitmapCallback}接口
 * 4、如果不需要对bitmap进行处理，可以实现{@link PicPathCallback}接口，就可以获取拍照的图片路径了。
 * 只需要传入Conext、SurfaceView，和注册一下拍照完成后的回调就可以使用了。如果需要对拍照的参数进行设置，
 * 可以在构造方法传入CameraOpenListener对象，该对象的监听方法会在照相机打开后执行相应方法
 * Create by yanyunpeng
 * Date: 2016/7/22 11:11
 */
public class CameraHelper implements SurfaceHolder.Callback {
    private Context mContext;
    /* 对焦方式 */
    private String focusMode = Camera.Parameters.FOCUS_MODE_FIXED;
    /* 标示使用的哪个摄像头,0后置摄像头，1前置摄像头,默认值为后置 */
    private int cameraId = CameraInfo.CAMERA_FACING_BACK;
    /* 是否打开闪光灯 */
    private boolean isFlashTouch;
    /* 是否正在预览 */
    private boolean isPreviewOn;
    /* 相机支持的拍照尺寸 */
//    private List<Size> supPicSize;
    /* 相机支持的预览尺寸 */
    private List<Size> supPreSize;

    /* 拍照时返回Bitmap的回调 */
    private BitmapCallback bitmapCallback;
    /* 拍照时返回照片路径的回调 */
    private PicPathCallback picPathCallback;
    /* 自动对焦回调 */
    private AnimationAutoFocusCallback autoFocusCallback;
    /* 照相机打开监听 */
    private CameraOpenListener cameraOpenListener;

    private Camera mCamera;
    private SurfaceView surfaceView;
    private AbsoluteLayout absoluteLayout;
    private SurfaceHolder mHolder;

    private int defaultScreenResolution = -1;
    private float aspectRatio;      //屏幕宽高比

    private Handler handler;

    private CameraHelper(Context context, SurfaceView surfaceView){
        this.mContext = context;
        this.surfaceView = surfaceView;

        init();
    }

    public CameraHelper(Context context, SurfaceView surfaceView,  CameraOpenListener cameraOpenListener){
        this.mContext = context;
        this.surfaceView = surfaceView;
        this.cameraOpenListener = cameraOpenListener;

        init();
    }

    public CameraHelper(Context context, SurfaceView surfaceView, AbsoluteLayout absoluteLayout){
        this(context, surfaceView);
        this.absoluteLayout = absoluteLayout;
    }


    public CameraHelper(Context context, SurfaceView surfaceView, AbsoluteLayout absoluteLayout,
                        CameraOpenListener cameraOpenListener) {
        this(context, surfaceView, cameraOpenListener);
        this.absoluteLayout = absoluteLayout;
    }

    /**
     * 检查设备是否存在摄像头
     * @param context 系统上下文
     * @return true：有摄像头；false：没有摄像头
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有前置摄像头,应该放在一个系统硬件辅助类中
     *
     * @return true：有前置摄像头；false:没有
     */
    public static boolean existFrontCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化设置
     */
    private void init() {
        mHolder = this.surfaceView.getHolder();                           // 获得句柄
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);        // surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        mHolder.addCallback(this);                                          // 添加回调

        aspectRatio = DisplayUtils.getScreenWith(mContext) / (DisplayUtils.getScreenHeight(mContext) + 0f);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleCameraOpen();
            }
        };

        openAndInitCamera();
    }

    private void handleCameraOpen() {
        if (mCamera != null) {
            supPreSize = mCamera.getParameters().getSupportedPreviewSizes();        //这个味道不好
            startPreview();
            if (cameraOpenListener != null) {
                cameraOpenListener.onCameraOpenSuc(mCamera);
            }
        } else {
            releaseCamera();
            if (cameraOpenListener != null) {
                cameraOpenListener.onCameraOpenFail();
            }
        }
    }

    private void openAndInitCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mCamera = Camera.open(cameraId);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    mCamera = null;
                }
            }
        }).start();
    }

    /**
     * 获取照相机支持拍摄的所有照片尺寸
     * Create by yanyunpeng
     * Date: 2016/7/22 11:24
     */
    public List<Camera.Size> getResolutionList() {
        return mCamera == null ? null : supPreSize;
    }

    /**
     * 获取照相机参数,实时获取
     * Create by yanyunpeng
     * Date: 2016/7/22 13:53
     */
    public Camera.Parameters getCameraParameters() {
        if (mCamera == null) return null;
        return mCamera.getParameters();
    }

    /**
     * 设置照相机参数
     * @param params 照相机的参数
     */
    public void setCameraParamerers(Camera.Parameters params) {
        if (mCamera == null) return;
        mCamera.setParameters(params);
    }

    /**
     * 是否打开闪光灯，如果为true，则在拍照的时候会打开闪光灯。对拍摄视频打开闪光灯需要实时
     * 改变闪光灯的状态，常亮还是关闭。
     * @return true:打开；false:没打开
     */
    public boolean isFlashTouch() {
        return isFlashTouch;
    }

    /**
     * 设置拍照时是否打开闪光灯
     * @param isFlashTorch true:打开，false:关闭
     */
    public void setFlashTouch(boolean isFlashTorch) {
        this.isFlashTouch = isFlashTorch;
    }

    /**
     * 设置camera显示取景画面,并预览，设置相机初始化配置
     */
    public void startPreview() {
        try {
            if (isPreviewOn) {
                mCamera.stopPreview();
                isPreviewOn = false;
            }
            updateCameraParameters();                   // 相机参数初始化设置

            if (this.absoluteLayout != null) {       //设置自动对焦
                autoFocusCallback = new AnimationAutoFocusCallback(mContext, this, absoluteLayout);
                this.absoluteLayout.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isPreviewOn) return false;
                        if (autoFocusCallback != null) {
                            if (cameraId == 0) {
                                autoFocusCallback.focusOnTouch(event, 0);       //自动对焦
                            } else {
                                autoFocusCallback.focusOnTouch(event, 3);       //固定对焦
                            }
                        }
                        return true;
                    }
                });
            }
            mCamera.setPreviewDisplay(mHolder);       // 布局绑定
            isPreviewOn = true;

            mCamera.startPreview();                    // 开始预览
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.cancelAutoFocus();
                }
            });
        } catch (Exception exception) {                // 360手机卫士拦截会报java.lang.RuntimeException:
            // Method called after release()
            releaseCamera();
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewOn = false;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始拍照
     */
    public void takePicture() {
        if (mCamera == null) {
            return;
        }
        Parameters params = mCamera.getParameters();
        // 闪光灯是否打开
        if (isFlashTouch) {
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        }
        // 设置旋转角度
        if (cameraId == CameraInfo.CAMERA_FACING_BACK) {// 后置摄像头
            params.setRotation(90);
        } else if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {// 前置摄像头
            params.setRotation(270);
        }
        mCamera.setParameters(params);

        try {
            mCamera.takePicture(null, null, new PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    if (data == null) {
                        return;
                    }
                    Bitmap pic = BitmapUtil.byteToBitmap(data, -1, -1);
                    if (pic.getWidth() > pic.getHeight()) {
                        // 设置角度
                        int rotation = 0;
                        if (cameraId == CameraInfo.CAMERA_FACING_BACK) {
                            rotation = 90;
                        } else if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {
                            rotation = 270;
                        }
                        pic = BitmapUtil.rotateBitmap(pic, rotation);
                    }
                    if (picPathCallback != null) {
                        picPathCallback.onPicPathCallback(BitmapUtil.saveBitmapToFile(pic, null, null));
                    } else if (bitmapCallback != null) {
                        bitmapCallback.onBitmapCallback(pic);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
    }

    /**
     * 一般情况下，CameraHelper是在Activity的onResume()方法中创建，拍完照片后在新的Activity中进行照片
     * 预览和对照片进行操作。如果拍完照后在与SurfaceView相同的页面进行照片预览和操作，预览照片时会导致
     * SurfaceView失效，当再次回到拍照预览时，会导致预览失败，此时需要调用此方法，重新打开摄像头，重新
     * 进行拍照预览。
     * @param surfaceView
     */
    public void rebuildSurface(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        init();
        startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
//        mHolder = null;
//        surfaceView = null;
    }

    /**
     * 设置更新相机参数
     */
    private void updateCameraParameters() {
        Camera.Parameters parameters = mCamera.getParameters();
        //设置屏幕方向
        setCameraOrientation(parameters);
        // 设置图片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //设置对焦模式
        setFocusMode(parameters);

        Size mPreviewSize = CameraParamUtil.getInstance().getProperPreviewSize(supPreSize, aspectRatio);
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        Size defaultSize = CameraParamUtil.getInstance().getProperPictureSize(parameters.getSupportedPictureSizes(), aspectRatio);
        parameters.setPictureSize(defaultSize.width, defaultSize.height);

        mCamera.setParameters(parameters);
    }

    /**
     * 设置屏幕方向
     * @param parameters 相机参数
     */
    private void setCameraOrientation(Parameters parameters) {
        if (mContext.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "portrait"); // 竖屏
            parameters.set("rotation", 90);
            mCamera.setDisplayOrientation(90);
        } else {
            parameters.set("orientation", "landscape");// 横屏
            mCamera.setDisplayOrientation(0);
        }
    }

    /**
     * 设置相机拍照尺寸
     * @param size
     */
    public void setPicSize(Camera.Size size) {
        if (size == null || mCamera == null) return;
        Parameters param = mCamera.getParameters();
        param.setPictureSize(size.width, size.height);
        mCamera.setParameters(param);
    }

    /**
     * 设置SurfaceView预览照片尺寸
     * @param size
     */
    public void setPreSize(Camera.Size size) {
        if (size == null || mCamera == null) return;
        Parameters param = mCamera.getParameters();
        param.setPreviewSize(size.width, size.height);
        mCamera.setParameters(param);
    }

    /**
     * 设置好相机的对焦方式，如果相机支持自动对焦，则使用自动对焦方式
     *
     * @param parameters 相机参数
     */
    private void setFocusMode(Parameters parameters) {
        List<String> focusModes = parameters.getSupportedFocusModes();
        String[] preferredModes = new String[]{Parameters.FOCUS_MODE_CONTINUOUS_PICTURE, Parameters.FOCUS_MODE_AUTO,
                Parameters.FOCUS_MODE_FIXED};
        if (focusModes.contains(Parameters.FOCUS_MODE_AUTO)) {
            focusMode = Parameters.FOCUS_MODE_AUTO;
            parameters.setFocusMode(focusMode);
        } else {
            for (int i = 0; i < preferredModes.length; i++) {
                focusMode = preferredModes[i];
                if (focusModes.contains(focusMode)) {
                    parameters.setFocusMode(focusMode);
                    break;
                } else {
//                    Logger.d("info", "unsupported, skipping to next...");
                }
            }
        }
    }

    /**
     * 获取预览尺寸
     * @return
     */
    public Camera.Size getPreviewSize() {
        //获取摄像头的所有支持的分辨率
        List<Size> resolutionList = supPreSize;
        Camera.Size s = null;
        if (resolutionList != null && resolutionList.size() > 0) {
            Collections.sort(resolutionList, new ResolutionComparator());
            if (defaultScreenResolution == -1) {
                boolean hasSize = false;
                //如果不支持设为中间的那个
                if (!hasSize) {
                    int mediumResolution = resolutionList.size() / 2;
                    if (mediumResolution >= resolutionList.size())
                        mediumResolution = resolutionList.size() - 1;
                    s = resolutionList.get(mediumResolution);
                }
            } else {
                if (defaultScreenResolution >= resolutionList.size())
                    defaultScreenResolution = resolutionList.size() - 1;
                s = resolutionList.get(defaultScreenResolution);
            }
        }
        return s;
    }

    /**
     * 停止预览视频
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        isPreviewOn = false;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public int getCameraFacing() {
        return cameraId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public void setBitmapCallback(BitmapCallback bitmapCallback) {
        this.bitmapCallback = bitmapCallback;
    }

    public void setPicPathCallback(PicPathCallback picPathCallback) {
        this.picPathCallback = picPathCallback;
    }

    public interface CameraOpenListener {
        void onCameraOpenSuc(Camera camera);

        void onCameraOpenFail();
    }

    public interface CameraOpenFailListener {
        void onCameraOpenFailCallback();
    }

    public interface BitmapCallback {
        void onBitmapCallback(Bitmap bitmap);
    }

    public interface PicPathCallback {
        void onPicPathCallback(String path);
    }

    /**
     * 预览尺寸比较
     */
    public static class ResolutionComparator implements Comparator<Size>, Serializable {
        @Override
        public int compare(Camera.Size size1, Camera.Size size2) {
            if (size1.height != size2.height)
                return size1.height - size2.height;
            else
                return size1.width - size2.width;
        }
    }

    /**
     * 默认的照相机打开失败回调，显示提示信息
     */
    private class DefaultCameraOpenFailListener implements CameraOpenFailListener {
        public DefaultCameraOpenFailListener() {
        }

        @Override
        public void onCameraOpenFailCallback() {
            Toast.makeText(mContext, "打开照相机失败", Toast.LENGTH_SHORT).show();
        }
    }
}
