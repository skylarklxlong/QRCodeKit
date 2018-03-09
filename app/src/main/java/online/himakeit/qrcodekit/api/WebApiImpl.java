package online.himakeit.qrcodekit.api;

import online.himakeit.qrcodekit.callback.WebNetCallBack;
import online.himakeit.qrcodekit.model.AppUpdateInfo;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class WebApiImpl {

    /**
     * 获取apk更新
     *
     * @param callBack
     */
    public static void getTheLastAppInfo(WebNetCallBack<AppUpdateInfo> callBack) {
        ApiManager.getInstence().getWebServiceApi().getTheLastAppInfo().enqueue(callBack);
    }
}
