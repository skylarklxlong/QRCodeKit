package online.himakeit.qrcodekit.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class WebNetHelper {
    public static Map<String, String> getApiCommonParams(String method) {
        Map params = new HashMap(10);
        params.put("method", method);
        return params;
    }
}
