package online.himakeit.qrcodekit.ui.view;

import android.view.animation.Animation;

import online.himakeit.qrcodekit.model.AppUpdateInfo;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public interface ISplashView {
    /**
     * 背景图片动画
     *
     * @param animation
     */
    void animateBackgroundImage(Animation animation);

    /**
     * 初始化数据
     *
     * @param versionName
     * @param copyright
     * @param backgroundResId
     */
    void initializeViews(String versionName, String copyright, int backgroundResId);

    /**
     * 前往主界面
     */
    void navigateToHomePage();

    /**
     * 显示更新对话框
     *
     * @param appUpdateInfo
     */
    void showAppUpdateDialog(AppUpdateInfo appUpdateInfo);

}
