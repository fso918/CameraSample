package com.yyp.mysample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author liujianjian on 15/8/12
 * 下午3:09.
 */
public class BitmapUtil {

	/**
	 * 将Bitmap存储到文件中
	 * @param bmp 需要存储bitmap
	 * @param format 照片的压缩格式，此值传入null时，会使用默认的JPG压缩格式，仅支持JPG和PNG格式
	 * @param path 照片存储路径，默认为/yunniao/temp目录
	 * @return
	 */
	public static String saveBitmapToFile(Bitmap bmp, Bitmap.CompressFormat format, String path) {
		if (bmp == null || bmp.isRecycled()) return "";
		if (format == null) format = Bitmap.CompressFormat.JPEG;
		if (format != Bitmap.CompressFormat.PNG && format != Bitmap.CompressFormat.JPEG) {
			throw new RuntimeException("图片格式不支持!");
		}
		if (path == null || "".equals(path.trim())) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mysample" + File
					.separator;
		}
		File fileFolder = new File(path);

		String fileName = System.currentTimeMillis() + getPicSuffix(format);

		if (!fileFolder.exists()) {
			if(!fileFolder.mkdir()){
				Log.i("TEST","mk folder fail!");
			}
			;
		}

		File pic = new File(fileFolder, fileName);
		try {
			bmp.compress(format, 80, new FileOutputStream(pic));
			path = pic.getAbsolutePath();
		} catch (FileNotFoundException e) {
			path = "";
			e.printStackTrace();
		} catch (IOException e) {
			path = "";
		} finally {
			bmp.recycle();
		}
		return path;
	}

	/**
	 * 根据图片压缩格式获取文件名后缀
	 *
	 * @param format 文件压缩格式
	 * @return 文件后缀名，默认返回“.jpg”
	 */
	public static String getPicSuffix(Bitmap.CompressFormat format) {
		switch (format) {
			case PNG:
				return ".png";
			case JPEG:
				return ".jpg";
		}
		return ".jpg";
	}

	/**
	 * @param byteArray
	 * @return
	 * @category 字节转化Bitmap
	 */
	public static Bitmap byteToBitmap(byte[] byteArray, int width, int height) {
		if (byteArray.length != 0) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inScaled = false;
			BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
			// 计算图片缩放比例
			int minSideLength = Math.min(width, height);
			if (width > 0 && height > 0) {
				options.inSampleSize = computeSampleSize(options, minSideLength, width * height);
			}
			options.inJustDecodeBounds = false;
			options.inInputShareable = true;
			options.inPurgeable = true;

			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
		} else {
			return null;
		}
	}

	/**
	 * 裁切Bitmap
	 * @param bitmap    需要裁切的Bitmap
	 * @param cutLeft   左边需要裁切掉的像素
	 * @param cutTop    上边需要裁切的像素
	 * @param cutRight  右边需要裁切的像素
	 * @param cutBottom 底部需要裁切的像素
	 * @return 裁切后的Bitmap
	 */
	public static Bitmap cutBitmap(Context context, Bitmap bitmap, int cutLeft, int cutTop, int cutRight, int cutBottom) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;;
		int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		int cutLeftPx = cutLeft * width / screenWidth;
		int cutTopPx = cutTop * height / screenHeight;
		int descWidth = width - cutLeftPx - cutRight * width / screenWidth;
		int descHeight = height - cutTopPx - cutBottom * height / screenHeight;
		return Bitmap.createBitmap(bitmap, cutLeftPx, cutTopPx, descWidth, descHeight);
	}


	/**
	 * 根据容器宽高来计算图片压缩比例 加载大图时做处避免内存溢出
	 *
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算的初始样本大小
	 *
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * @param bitmap
	 * @param Angle  角度
	 * @return 旋转后的图片
	 * @category bitmap旋转
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int Angle) {
		Matrix m = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		m.setRotate(Angle);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
		return bitmap;
	}

}
