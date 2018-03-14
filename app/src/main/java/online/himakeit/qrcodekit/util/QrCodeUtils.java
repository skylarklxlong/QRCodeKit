package online.himakeit.qrcodekit.util;

import android.graphics.Bitmap;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;

/**
 * @author：LiXueLong
 * @date：2018/3/14
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class QrCodeUtils {

    /**
     * 生成二维码
     *
     * @param contents          二维码内容
     * @param size              二维码大小
     * @param margin            二维码白边大小
     * @param dotScale          圆点大小（值越大点越大）
     * @param colorDark         二维码背景色，数据区域以及四个基准点
     * @param colorLight        二维码前景色，非数据区域
     * @param background        二维码背景图片
     * @param whiteMargin       背景图片白边
     * @param autoColor         自动获取颜色，true代表从背景中获取颜色，如果设置为false的话就必须要设置前景色和背景色
     * @param binarize          二值化
     * @param binarizeThreshold 二值化范围
     * @param roundedDD         圆点数据
     * @param logoImage         logo图片
     * @param logoMargin        logo白边
     * @param logoCornerRadius  logo圆角半径
     * @param logoScale         logo大小
     * @param callback          回调
     */
    public static void generate(String contents, int size, int margin,
                                float dotScale, int colorDark, int colorLight, Bitmap background,
                                boolean whiteMargin, boolean autoColor, boolean binarize,
                                int binarizeThreshold, boolean roundedDD, Bitmap logoImage,
                                int logoMargin, int logoCornerRadius, float logoScale, AwesomeQRCode.Callback callback) {
        new AwesomeQRCode.Renderer().contents(contents)
                .size(size).margin(margin).dotScale(dotScale)
                .colorDark(colorDark).colorLight(colorLight)
                .background(background).whiteMargin(whiteMargin)
                .autoColor(autoColor).binarize(binarize)
                .binarizeThreshold(binarizeThreshold).roundedDots(roundedDD)
                .logo(logoImage).logoMargin(logoMargin).logoRadius(logoCornerRadius)
                .logoScale(logoScale).renderAsync(callback);

    }
}
