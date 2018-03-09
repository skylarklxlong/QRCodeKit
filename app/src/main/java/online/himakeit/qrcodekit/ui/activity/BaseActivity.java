package online.himakeit.qrcodekit.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
