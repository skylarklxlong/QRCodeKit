package online.himakeit.qrcodekit;

import android.app.Application;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import online.himakeit.qrcodekit.util.LogUtils;
import online.himakeit.qrcodekit.util.NetUtils;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class AppContext extends Application {

    private static final String TAG = "AppContext";

    /**
     * 全局应用的上下文
     */
    static AppContext mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;

        initLeakMemory();
        initToasts();
        initUmengAnalytics();
    }

    private void initLeakMemory() {
        if (BuildConfig.DEBUG) {
            //memory leak testing
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
            // Normal app init code...
        }
    }

    private void initToasts() {
        Toasts.register(this);
    }

    /**
     * 获取全局的AppContext
     *
     * @return
     */
    public static AppContext getAppContext() {
        return mAppContext;
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

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        client.readTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        client.connectTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        //设置缓存路径
        File httpCacheDirectory = new File(mAppContext.getCacheDir(), "okhttpCache");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        client.cache(cache);
        //设置拦截器
        client.addInterceptor(LoggingInterceptor);
        client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        /**
         * 添加拦截器 chuck 记录客户端所有的网络连接
         * 可以使用 new ChuckInterceptor(mAppContext).showNotification(false)
         * 来取消显示。
         *
         * 使用 Chuck.getLaunchIntent();来在任何地方打开Chunck界面
         */
        client.addInterceptor(new ChuckInterceptor(mAppContext).showNotification(BuildConfig.DEBUG));
        return client.build();
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //方案一：有网和没有网都是先读缓存
//                Request request = chain.request();
//                Log.i(TAG, "request=" + request);
//                Response response = chain.proceed(request);
//                Log.i(TAG, "response=" + response);
//
//                String cacheControl = request.cacheControl().toString();
//                if (TextUtils.isEmpty(cacheControl)) {
//                    cacheControl = "public, max-age=60";
//                }
//                return response.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();

            //方案二：无网读缓存，有网根据过期时间重新请求
            boolean netWorkConection = NetUtils.hasNetWorkConection(AppContext.getAppContext());
            Request request = chain.request();
            if (!netWorkConection) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (netWorkConection) {
                /**
                 * 有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                 */
                String cacheControl = request.cacheControl().toString();
                response.newBuilder()
                        /**
                         * 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                         */
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };

    private static final Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.show(TAG, "-----LoggingInterceptor----- :\nrequest url:" + request.url() + "\ntime:" + (t2 - t1) / 1e6d + "\nbody:" + content + "\n");
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    };
}
