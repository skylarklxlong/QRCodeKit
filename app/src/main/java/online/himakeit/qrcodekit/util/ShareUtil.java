package online.himakeit.qrcodekit.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import online.himakeit.qrcodekit.config.Config;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class ShareUtil {
    public static void share(Context context, String extraText) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        textIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        textIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(textIntent, "分享"));
    }

    public static void share(Context context, int stringRes) {
        share(context, context.getString(stringRes));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_SEND);
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        imageIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(imageIntent, title));
    }

    public static void shareWeb(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.setData(Uri.parse(url));
        context.startActivity(mIntent);
    }

    /**
     * 此方法支持应用宝、360手机助手、豌豆荚、小米商店等主流的应用市场，
     * 当手机上面装有多个应用市场时，则会弹出菜单让用户选择调转到哪个市场
     *
     * @param context
     * @param packageName
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(mIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toasts.showShort("未发现应用市场,即将跳转到网页版");
            shareWeb(context, Config.FIR_IM_APP_URL);
            LogUtils.e("未发现应用市场");
        }
    }

    /**
     * 跳转到应用宝
     *
     * @param context
     * @param packageName
     */
    public static void goToTencentMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 三星市场
     *
     * @param context
     * @param packageName
     */
    public static void goToSamsungappsMarket(Context context, String packageName) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName);
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 乐视的应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToLeTVStore(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", packageName);
        context.startActivity(intent);
    }
}
