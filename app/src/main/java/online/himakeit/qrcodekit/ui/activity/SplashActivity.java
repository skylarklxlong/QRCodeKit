package online.himakeit.qrcodekit.ui.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.maning.updatelibrary.InstallUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.model.AppUpdateInfo;
import online.himakeit.qrcodekit.ui.common.BaseActivityFullScreen;
import online.himakeit.qrcodekit.ui.presenter.SplashPresenterImpl;
import online.himakeit.qrcodekit.ui.view.ISplashView;
import online.himakeit.qrcodekit.util.ApkUtils;
import online.himakeit.qrcodekit.util.BitmapUtil;
import online.himakeit.qrcodekit.util.DialogUtil;
import online.himakeit.qrcodekit.util.LogUtils;
import online.himakeit.qrcodekit.util.NetUtils;
import online.himakeit.qrcodekit.util.NotifyUtil;
import online.himakeit.qrcodekit.util.SharePreUtil;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date:2018/3/8-19:52
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：MainActivity
 */
public class SplashActivity extends BaseActivityFullScreen implements ISplashView {

    @BindView(R.id.splash_image)
    ImageView mSplashImage;
    @BindView(R.id.splash_version_name)
    TextView mVersionName;
    @BindView(R.id.splash_copyright)
    TextView mCopyright;

    SplashPresenterImpl splashPresenter = null;
    private NotifyUtil notifyUtils;
    private MaterialDialog dialogUpdate;
    private boolean isCancel = false;
    WeakReference<Bitmap> bitmapWeakReference;

    private static String[] NEED_PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            splashPresenter = new SplashPresenterImpl(this, this);
            splashPresenter.initialized();
        } else {
            DialogUtil.showMyDialog(this, "提示", "在生成二维码的过程中如果需要导入图片，那么就必须要读写SD卡的权限", "好哒~", "没问题", new DialogUtil.OnDialogClickListener() {
                @Override
                public void onConfirm() {
                    ActivityCompat.requestPermissions(SplashActivity.this, NEED_PERMISSIONS, 0x001);
                }

                @Override
                public void onCancel() {
                    ActivityCompat.requestPermissions(SplashActivity.this, NEED_PERMISSIONS, 0x001);
                }
            });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x001) {
            if (grantResults.length > 0 && grantResults[6] == PackageManager.PERMISSION_GRANTED) {
                Toasts.showShort("权限申请成功！");
            } else {
                Toasts.showShort("权限申请失败！");
            }
            splashPresenter = new SplashPresenterImpl(this, this);
            splashPresenter.initialized();
        }
    }

    @Override
    public void animateBackgroundImage(Animation animation) {
        mSplashImage.startAnimation(animation);
    }

    @Override
    public void initializeViews(String versionName, String copyright, int backgroundResId) {
        mCopyright.setText(copyright);
        mVersionName.setText(versionName);

        Bitmap bitmap = BitmapUtil.compressBitmap(SplashActivity.this, backgroundResId);
        bitmapWeakReference = new WeakReference<Bitmap>(bitmap);
        if (bitmapWeakReference != null) {
            mSplashImage.setImageBitmap(bitmapWeakReference.get());
        }
    }

    @Override
    public void navigateToHomePage() {
        if (!SharePreUtil.getBooleanData(this, Config.APP_NEED_UPDATE, false) || isCancel) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void showAppUpdateDialog(final AppUpdateInfo appUpdateInfo) {
        String title = "检测到新版本:V" + appUpdateInfo.getVersionShort();
        Double appSize = Double.parseDouble(appUpdateInfo.getBinary().getFsize() + "") / 1024 / 1024;
        DecimalFormat df = new DecimalFormat(".##");
        String resultSize = df.format(appSize) + "M";
        boolean isWifi = NetUtils.isWifiConnected(this);
        String content = appUpdateInfo.getChangelog() +
                "\n\n新版大小：" + resultSize +
                "\n当前网络：" + (isWifi ? "wifi" : "非wifi环境(注意)");

        DialogUtil.showMyDialog(this,
                title, content, "立马更新", "稍后更新",
                new DialogUtil.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        //更新版本
                        showDownloadDialog(appUpdateInfo);
                    }

                    @Override
                    public void onCancel() {
                        isCancel = true;
                        navigateToHomePage();
                    }
                });
    }

    private void showDownloadDialog(AppUpdateInfo appUpdateInfo) {
        dialogUpdate = new MaterialDialog.Builder(SplashActivity.this)
                .title("正在下载最新版本")
                .content("请稍等")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .progress(false, 100, false)
                .negativeText("后台下载")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startNotifyProgress();
                    }
                })
                .show();

        new InstallUtils(SplashActivity.this, appUpdateInfo.getInstall_url(),
                ApkUtils.getAppName(SplashActivity.this) + "_" + appUpdateInfo.getVersionShort(),
                new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        LogUtils.i("installAPK-----onStart");
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(0);
                        }
                    }

                    @Override
                    public void onComplete(String path) {
                        LogUtils.i("installAPK----onComplete:" + path);
                        /**
                         * 安装APK工具类
                         * @param context       上下文
                         * @param filePath      文件路径
                         * @param authorities   ---------Manifest中配置provider的authorities字段---------
                         * @param callBack      安装界面成功调起的回调
                         */
                        InstallUtils.installAPK(SplashActivity.this, path, getPackageName() + ".fileProvider", new InstallUtils.InstallCallBack() {
                            @Override
                            public void onSuccess() {
                                SharePreUtil.saveBooleanData(SplashActivity.this, Config.APP_NEED_UPDATE, false);
                                Toasts.showShort("正在安装程序");
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toasts.showShort("安装失败:" + e.toString());
                            }
                        });
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                        }
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgressComplete();
                            notifyUtils.clear();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        LogUtils.i("installAPK-----onLoading:-----total:" + total + ",current:" + current);
                        int currentProgress = (int) (current * 100 / total);
                        if (dialogUpdate != null) {
                            dialogUpdate.setProgress(currentProgress);
                        }
                        if (notifyUtils != null) {
                            notifyUtils.setNotifyProgress(100, currentProgress, false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (dialogUpdate != null && dialogUpdate.isShowing()) {
                            dialogUpdate.dismiss();
                        }
                        if (notifyUtils != null) {
                            notifyUtils.clear();
                        }
                    }
                }).downloadAPK();

    }

    /**
     * 开启通知栏
     */
    private void startNotifyProgress() {
        //设置想要展示的数据内容
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.zero;
        String ticker = "正在下载" + ApkUtils.getAppName(SplashActivity.this) + "更新包...";
        //实例化工具类，并且调用接口
        notifyUtils = new NotifyUtil(this, 0);
        notifyUtils.notify_progress(rightPendIntent, smallIcon, ticker, ApkUtils.getAppName(SplashActivity.this) + "下载", "正在下载中...", false, false, false);
    }

}
