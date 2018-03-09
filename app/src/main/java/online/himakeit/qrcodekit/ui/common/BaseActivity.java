package online.himakeit.qrcodekit.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.umeng.analytics.MobclickAgent;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class BaseActivity extends AppCompatActivity {

    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDialog();
    }

    private void initDialog() {
        svProgressHUD = new SVProgressHUD(this);
    }

    public void showProgressDialog() {
        dissmissProgressDialog();
        svProgressHUD.showWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    public void showProgressDialog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showProgressDialog();
        } else {
            dissmissProgressDialog();
            svProgressHUD.showWithStatus(msg, SVProgressHUD.SVProgressHUDMaskType.Black);
        }
    }

    public void showProgressSuccess(String msg) {
        dissmissProgressDialog();
        svProgressHUD.showSuccessWithStatus(msg, SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    public void showProgressError(String msg) {
        dissmissProgressDialog();
        svProgressHUD.showErrorWithStatus(msg, SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    public void dissmissProgressDialog() {
        if (svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。)
         */
        MobclickAgent.onPageStart(getClass().getName());
        /**
         * Session统计
         * 用来统计应用时长的
         */
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。
         */
        MobclickAgent.onPageEnd(getClass().getName());
        /**
         * Session统计
         * 用来统计应用时长的
         */
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
