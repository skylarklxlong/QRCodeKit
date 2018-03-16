package online.himakeit.qrcodekit.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author：LiXueLong
 * @date：2018/3/15
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class BitmapUtil {

    public static Bitmap compressBitmap(Context context, int imgRes) {
        /**
         * 设置参数
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imgRes, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        /**
         * 默认像素压缩比例，压缩为原图的1/2
         */
        int sampleSize = 2;
        /**
         * 原图最小边长
         */
        int minLen = Math.min(outHeight, outWidth);

        /**
         * 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
         */
        if (minLen > 200) {
            /**
             * 计算像素压缩比例
             */
            float ratio = (float) minLen / 200.0f;
            sampleSize = (int) ratio;
            LogUtils.show("sampleSize：" + sampleSize);
        }
        /**
         * 计算好压缩比例后，这次可以去加载原图了
         */
        options.inJustDecodeBounds = false;
        /**
         * 设置为刚才计算的压缩比例
         */
        options.inSampleSize = sampleSize;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes, options);

        return bitmap;
    }
}
