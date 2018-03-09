package online.himakeit.qrcodekit.api;

import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.model.AppUpdateInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public interface WebServiceApi {

    /**
     * 获取fir.im中的Love的最新版本
     *
     * @return
     */
    @Headers("Cache-Control: public, max-age=3600")
    @GET(Config.URL_APP_UPDATE_INFO)
    Call<AppUpdateInfo> getTheLastAppInfo();
}
