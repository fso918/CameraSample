
package com.yyp.mysample.feature.camera;

import android.hardware.Camera.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 关于照相机参数处理的类，主要用于获取适合的设置，如预览像素大小，拍照相片大小等。
 * Create by yanyunpeng
 * Date: 2016/7/23 8:58
 */
public class CameraParamUtil {
    private static CameraParamUtil myCamPara;
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();

    private CameraParamUtil() {
    }

    public static CameraParamUtil getInstance() {
        if (myCamPara == null) {
            myCamPara = new CameraParamUtil();
        }
        return myCamPara;
    }

    public Size getProperPreviewSize(List<Size> list, float aspect) {
        return getProperSize(list, aspect);
    }

    public Size getProperPictureSize(List<Size> list, float aspect) {
        return getProperSize(list, aspect);
    }

    /**
     * 获取默认的拍照图片大小，如果相机支持1920*1080的尺寸，则用1920*1080的尺寸，
     * 否则用最接近1920*1080的尺寸
     * @param list 照相机支持的所有拍照尺寸
     * @return
     */
    public Size getDefaultPictureSize1(List<Size> list) {
        for (Size size : list) {
            if (size.width == 1920 && size.height == 1080) {
                return size;
            }
        }
        Collections.sort(list, sizeComparator);

        return list.get(binarySearch(list, 1920, 0, list.size() - 1));
    }

    /**
     * 计算最佳尺寸，分辨率有相等>比例相等>比例差值最小
     * @param list
     * @return
     */
    public Size getProperSize(List<Size> list, float aspectRatio) {
        Collections.sort(list, sizeComparator);
        List<Size> descList = new ArrayList<Size>();
        for (Size size : list) {
            //最佳匹配
            if (size.height == 1080 && Math.abs(size.height / (size.width + 0f) - aspectRatio) <= 0.01) {
                return size;
            }
            if (Math.abs((Double.valueOf(size.height) / size.width)) - aspectRatio <= 0.01) {
                descList.add(size);
            }
        }
        if (descList.size() > 0) {
            return descList.get(descList.size() / 2);
        }

        int result = binarySearch(list, 1920, 0, list.size() - 1);

        return list.get(result);
    }

    /**
     * 二分查找
     * @param sortedList 目标List
     * @param width      目标宽
     * @param start      开始位置
     * @param end        结束位置
     * @return 下标
     */
    private int binarySearch(List<Size> sortedList, int width, int start, int end) {
        if (end - start <= 0) {
            return start;
        }
        int mid = (start + end) / 2;
        int widthTmp = sortedList.get(mid).width - width;
        if (Math.abs(widthTmp) == 0) {
            return mid;
        }
        if (widthTmp > 0) {
            return binarySearch(sortedList, width, mid + 1, end);
        } else {
            return binarySearch(sortedList, width, start, mid - 1);
        }
    }

    /**
     * 照相机Size比较器
     */
    private static class CameraSizeComparator implements Comparator<Size>, Serializable {
        public int compare(Size lhs, Size rhs) {
            return -(lhs.width - rhs.width);
        }
    }

}
