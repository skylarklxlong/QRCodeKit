package online.himakeit.qrcodekit.config;

import android.os.Environment;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des: 不同机型在设置图片时，加载与自己相同分辨率的drawable或者mipmap目录下的图片是最省内存的
 */
public class Config {
    /**
     * 获取APKinfo的地址：fir.im
     */
    public static final String URL_APP_UPDATE_INFO = "http://api.fir.im/apps/latest/5aa204d1959d695f6a536418?api_token=d242396c7ad3470a207b6c120a2ab0b5&type=android";
    public static final String BASE_URL = "http://api.fir.im";

    public static final String DEFAULT_ROOT = Environment.getExternalStorageDirectory() + "/Skylark";
    public static final String DEFAULT_ROOT_PATH = DEFAULT_ROOT;
    public static final String DEFAULT_SCREENSHOT_PATH = DEFAULT_ROOT + "/screenshot";

    public static final String APP_NEED_UPDATE = "need_update";

    public static final String APP_NAME_EN = "QRCodeKit";
    public static final String QRCODE_NORMAL_TYPE = "normal";
    public static final String QRCODE_LOGO_TYPE = "logo";
    public static final String QRCODE_AWESOME_TYPE = "awsome";
    public static final String WEIBO_URL = "https://weibo.com/u/5069128618";
    public static final String FIR_IM_APP_URL = "http://fir.im/qrcodekit";
}
