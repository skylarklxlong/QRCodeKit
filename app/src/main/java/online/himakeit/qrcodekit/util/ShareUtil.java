package online.himakeit.qrcodekit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class ShareUtil {
    public static void share(Context context, String extraText){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_SUBJECT,"分享");
        textIntent.putExtra(Intent.EXTRA_TEXT,extraText);
        textIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(textIntent,"分享"));
    }

    public static void share(Context context,int stringRes){
        share(context,context.getString(stringRes));
    }

    public static void shareImage(Context context, Uri uri, String title){
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_SEND);
        imageIntent.putExtra(Intent.EXTRA_STREAM,uri);
        imageIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(imageIntent,title));
    }
}
