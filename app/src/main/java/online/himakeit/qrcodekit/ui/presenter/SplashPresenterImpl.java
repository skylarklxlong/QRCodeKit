package online.himakeit.qrcodekit.ui.presenter;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Calendar;

import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.api.WebApiImpl;
import online.himakeit.qrcodekit.callback.WebNetCallBack;
import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.model.AppUpdateInfo;
import online.himakeit.qrcodekit.ui.view.ISplashView;
import online.himakeit.qrcodekit.util.ApkUtils;
import online.himakeit.qrcodekit.util.SharePreUtil;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class SplashPresenterImpl {

    Context context;
    ISplashView iSplashView;

    public SplashPresenterImpl(Context context, ISplashView iSplashView) {
        if (iSplashView == null) {
            throw new IllegalArgumentException("ISplashView must not be null!");
        }
        this.context = context;
        this.iSplashView = iSplashView;
    }

    public void initialized() {
        iSplashView.initializeViews(getVersionName(context), getCopyright(context), getBackgroundImageResID());
        Animation animation = getBackgroundImageAnimation(context);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                checkAppUpdate();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                try {
                    Thread.sleep(500);
                    iSplashView.navigateToHomePage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iSplashView.animateBackgroundImage(animation);
    }

    private void checkAppUpdate() {
        WebApiImpl.getTheLastAppInfo(new WebNetCallBack<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                //获取当前APP的版本号
                int newVersion;
                try {
                    newVersion = Integer.parseInt(appUpdateInfo.getBuild());
                } catch (Exception e) {
                    newVersion = 1;
                }

                if (ApkUtils.getVersionCode(context) < newVersion) {
                    SharePreUtil.saveBooleanData(context, Config.APP_NEED_UPDATE, true);
                    //需要版本更新
                    if (iSplashView != null) {
                        iSplashView.showAppUpdateDialog(appUpdateInfo);
                    }
                } else {
                    SharePreUtil.saveBooleanData(context, Config.APP_NEED_UPDATE, false);
                }
            }
        });
    }

    private int getBackgroundImageResID() {
        int resId;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 12) {
            resId = R.drawable.morning;
        } else if (hour > 12 && hour <= 18) {
            resId = R.drawable.afternoon;
        } else {
            resId = R.drawable.night;
        }
        return resId;
    }

    private String getCopyright(Context context) {
        return context.getResources().getString(R.string.splash_copyright);
    }

    private String getVersionName(Context context) {
        return String.valueOf("V " + ApkUtils.getVersionName(context));
    }

    private Animation getBackgroundImageAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_splash);
    }
}
