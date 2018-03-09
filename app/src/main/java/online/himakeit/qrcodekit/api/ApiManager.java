package online.himakeit.qrcodekit.api;

import online.himakeit.qrcodekit.AppContext;
import online.himakeit.qrcodekit.config.Config;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class ApiManager {

    private static ApiManager apiManager;
    private WebServiceApi webServiceApi;
    private static Object monitor = new Object();

    /**
     * 采用双重检查模式的单列
     *
     * @return
     */
    public static ApiManager getInstence() {
        if (apiManager == null) {
            synchronized (monitor) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    public WebServiceApi getWebServiceApi() {
        if (webServiceApi == null) {
            synchronized (monitor) {
                if (webServiceApi == null) {
                    webServiceApi = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(AppContext.defaultOkHttpClient())
                            .build()
                            .create(WebServiceApi.class);
                }
            }
        }
        return webServiceApi;
    }
}
