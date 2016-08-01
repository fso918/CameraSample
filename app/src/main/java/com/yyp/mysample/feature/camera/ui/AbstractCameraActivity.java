package com.yyp.mysample.feature.camera.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.mysample.Constant.Constant;
import com.yyp.mysample.R;
import com.yyp.mysample.feature.camera.AnimationAutoFocusCallback;
import com.yyp.mysample.feature.camera.CameraHelper;
import com.yyp.mysample.feature.camera.ui.view.RectCameraMaskView;
import com.yyp.mysample.utils.BitmapUtil;
import com.yyp.mysample.utils.DisplayUtils;
import com.yyp.mysample.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 照相机通用Activity
 * Create by yanyunpeng
 * Date: 2016/7/28 9:34
 */
public abstract class AbstractCameraActivity extends Activity implements CameraHelper.BitmapCallback, CameraHelper.CameraOpenListener{
    public static final int DISPLAY_MODE_CAMERA = 0;
    public static final int DISPLAY_MODE_PICTURE = 1;
    public static final int TAKE_PIC_MODE_MASK = 1;         //使用蒙版拍照
    public static final int TAKE_PIC_MODE_NOR = 0;          //不使用蒙版拍照

    private static String imageLocalPath = null;                //图片的本地存储地址
    protected int displayMode = DISPLAY_MODE_CAMERA;           //页面显示模式，0为显示相机预览，1为显示图片预览
    protected int takePictureMode = TAKE_PIC_MODE_NOR;         //0：不使用蒙版拍照；1：使用蒙版拍照
    protected int cutTop;
    protected int cutBottom;

    protected String title;
    protected String topTip;
    protected String bottomTip;

    protected ImageView podImageView;                             //预览拍摄的照片
    protected TextView takePhotoView;                             //拍照
    protected TextView useView;                                   //使用照片
    protected TextView rePhotoView;                               //重拍
    protected SurfaceView surfaceView;                            //拍照预览的View
    protected AbsoluteLayout autoFocusLayout;                     //自动对焦的Layout
    protected ImageView backIv;
    protected RectCameraMaskView maskView;
    protected TextView titleTv;                                 //页面标题
    protected TextView topTipTv;                                //上部提示
    protected TextView bottomTipTv;                             //底部提示

    protected boolean allowClickUse = false;                     //是否可以点击使用,默认不可以点击，需要拍完照后才可以
    protected boolean allowClickPhoto = true;                    //是否可以点击拍照
    protected boolean allowClickRePhoto = false;                 //是否可以点击重拍,默认不可以，需要拍完照后才可以
    protected CameraHelper cameraHelper;
    private AnimationAutoFocusCallback autoFocusCallback;        //自动对焦回调
    private Camera.Size picSize;
    private Camera.Size preSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_sample);

        podImageView = (ImageView) findViewById(R.id.pod_image);
        takePhotoView = (TextView) findViewById(R.id.take_photo);
        useView = (TextView) findViewById(R.id.use);
        rePhotoView = (TextView) findViewById(R.id.re_photo);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        autoFocusLayout = (AbsoluteLayout) findViewById(R.id.auto_focus_layout);
        maskView = (RectCameraMaskView) findViewById(R.id.camera_mask);
        backIv = (ImageView) findViewById(R.id.back);
        titleTv = (TextView) findViewById(R.id.title);

        onIntent(getIntent());

        titleTv.setText(title);

        takePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });

        useView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPictureComplete(imageLocalPath);
            }
        });

        rePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRePhoto(v);
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(v);
            }
        });


        RectCameraMaskView.DrawParameters parameters = maskView.getDrawParameters();
        cutTop = DisplayUtils.dip2px(this, 48);
        cutBottom = DisplayUtils.dip2px(this, 84);
        parameters.setCutTop(cutTop);
        parameters.setCutBottom(cutBottom);
        maskView.setDrawParameters(parameters);
    }

    protected void onIntent(Intent data) {
        takePictureMode = data.getIntExtra(Constant.EXTRA_CAMERA_PIC_MODE, TAKE_PIC_MODE_NOR);
        title = data.getStringExtra(Constant.EXTRA_TITLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayByMode(false);
    }

    private void adjustSurfaceView() {
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        params.height = DisplayUtils.getScreenHeight(this);
        params.width = DisplayUtils.getScreenWith(this);
    }

    /**
     * 按照拍照和预览模式显示页面
     * @param isRebuild
     */
    private void displayByMode(boolean isRebuild) {
        switch (displayMode) {
            case DISPLAY_MODE_CAMERA:
                allowClickUse = false;
                allowClickPhoto = true;
                allowClickRePhoto = false;
                adjustSurfaceView();
                if (!isRebuild) {
                    cameraHelper = new CameraHelper(this, surfaceView, autoFocusLayout, this);
                    cameraHelper.setBitmapCallback(this);
                    cameraHelper.setPreSize(preSize);
                    cameraHelper.startPreview();
                } else {
                    cameraHelper.rebuildSurface(surfaceView);
                }
                surfaceView.setVisibility(View.VISIBLE);
                takePhotoView.setVisibility(View.VISIBLE);

                useView.setVisibility(View.GONE);
                rePhotoView.setVisibility(View.GONE);
                podImageView.setVisibility(View.GONE);
                if (takePictureMode == TAKE_PIC_MODE_NOR) {
                    maskView.setVisibility(View.GONE);
                } else if (takePictureMode == TAKE_PIC_MODE_MASK) {
                    maskView.setVisibility(View.VISIBLE);
                }
                break;
            case DISPLAY_MODE_PICTURE:
                allowClickUse = true;
                allowClickPhoto = false;
                allowClickRePhoto = true;
                useView.setVisibility(View.VISIBLE);
                rePhotoView.setVisibility(View.VISIBLE);
                podImageView.setVisibility(View.VISIBLE);
                takePhotoView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraHelper.releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 跳转到照片预览页面，此时会导致Camera对象销毁
     *
     * @param imagePath 照片路径
     */
    public void toPreviewPage(String imagePath) {
        podImageView = (ImageView) findViewById(R.id.pod_image);
        podImageView.setImageURI(Uri.fromFile(new File(imagePath)));
        imageLocalPath = imagePath;

        //隐藏SurfaceView、拍照按钮，显示ImageView、使用和重拍按钮
        surfaceView.setVisibility(View.GONE);
        displayMode = DISPLAY_MODE_PICTURE;
        displayByMode(false);
    }

    private void takePhoto(View view) {
        if (allowClickPhoto) {
            allowClickPhoto = false;
            //点击了拍照后,使用和重拍都可以点击
            allowClickUse = true;
            allowClickRePhoto = true;
            cameraHelper.takePicture();
        }
    }

    /**
     * 对照相机返回的原始Bitmap进行裁剪
     * @param bitmap 照相机拍摄的原始照片Bitmap
     */
    @Override
    public void onBitmapCallback(Bitmap bitmap) {
        bitmap = cutBitmap(bitmap);
        String path = Constant.DIR;
        toPreviewPage(BitmapUtil.saveBitmapToFile(bitmap, null, path));
    }

    @Override
    public void onCameraOpenSuc(Camera camera) {
        initSize(camera);
    }

    @Override
    public void onCameraOpenFail() {
        Toast.makeText(this, "照相机打开失败", Toast.LENGTH_SHORT).show();
    }

    private void clickRePhoto(View view) {
        if (allowClickRePhoto) {
            displayMode = DISPLAY_MODE_CAMERA;
            displayByMode(true);
            deleteTmpFile();
        }
    }

    /**
     * 删除临时的拍照文件，在照片预览的时候，按重拍，将执行此方法
     */
    private void deleteTmpFile() {
        if (StringUtil.isEmpty(imageLocalPath)) return;
        File file = new File(imageLocalPath);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                Log.i("mysample", "delete file fail！");
            }
        }
    }

    private void closeActivity(View view) {
        switch (displayMode) {
            case DISPLAY_MODE_CAMERA:
                finish();
                break;
            case DISPLAY_MODE_PICTURE:
                displayMode = DISPLAY_MODE_CAMERA;
                displayByMode(false);
                break;
        }
    }

    private void initSize(Camera camera) {
        Camera.Parameters param = camera.getParameters();
        int sWidth = DisplayUtils.getScreenWith(this);
        int sHeight = DisplayUtils.getScreenHeight(this);
        float target = (sWidth + 0f) / sHeight;
        List<Camera.Size> sizes = param.getSupportedPictureSizes();
        List<Camera.Size> destSizes = new ArrayList<Camera.Size>();
        float tmp = 0f;
        for (Camera.Size size : sizes) {
            tmp = (size.height + 0f) / size.width;
            if (Math.abs(tmp - target) <= 0.01f) {
                destSizes.add(size);
            }
        }
        if (destSizes.size() > 0) {
            picSize = destSizes.get(destSizes.size() / 2);
        }
        destSizes.clear();
        sizes = param.getSupportedPreviewSizes();
        for (Camera.Size size : sizes) {
            tmp = (size.height + 0f) / size.width;
            if (Math.abs(tmp - target) <= 0.01f) {
                destSizes.add(size);
            }
        }
        if (destSizes.size() > 0) {
            preSize = destSizes.get(destSizes.size() / 2);
        }
    }

    private Bitmap cutBitmap(Bitmap bitmap) {
        bitmap = BitmapUtil.cutBitmap(this, bitmap, calLeftCut(), calTopCut(), calRightCut(), calBottomCut());
        return bitmap;
    }

    protected int calLeftCut() {
        if (takePictureMode == TAKE_PIC_MODE_MASK) {
            return maskView.getShadowLeft();
        }
        return 0;
    }

    protected int calRightCut() {
        if (takePictureMode == TAKE_PIC_MODE_MASK) {
            return maskView.getShadowLeft();
        }
        return 0;
    }

    protected int calTopCut() {
        if (takePictureMode == TAKE_PIC_MODE_MASK) {
            return maskView.getShadowTop() + cutTop;
        }
        return cutTop;
    }

    protected int calBottomCut() {
        if (takePictureMode == TAKE_PIC_MODE_MASK) {
            return maskView.getShadowTop() + cutBottom + DisplayUtils.getStatusHeight(this);
        }
        return cutBottom + DisplayUtils.getStatusHeight(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            closeActivity(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 此方法是为了实现拍照完成后业务的可扩展性，
     * 使用时可以根据自己的业务需求实现此方法。
     * {@link CameraActivity} 实现了此方法的默认行为，将拍照完成后的照片路径返回给调用的Activity
     *
     * @param imgPath 拍照完成后的照片路径
     */
    protected abstract void onPictureComplete(String imgPath);
}
