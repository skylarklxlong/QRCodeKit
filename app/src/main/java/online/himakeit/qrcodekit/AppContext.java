package online.himakeit.qrcodekit;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUmengAnalytics();
    }

    private void initUmengAnalytics() {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         * 或者：
         * UMConfigure.init(Context context, String appkey, String channel, int deviceType, String pushSecret);
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(BuildConfig.DEBUG);
        /**
         * 禁止默认的页面统计方式
         */
        MobclickAgent.openActivityDurationTrack(false);
        /**
         * 捕获程序崩溃日志
         */
        MobclickAgent.setCatchUncaughtExceptions(true);
    }
}
