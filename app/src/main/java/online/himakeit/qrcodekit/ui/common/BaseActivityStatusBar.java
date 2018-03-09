package online.himakeit.qrcodekit.ui.common;

import android.os.Bundle;

import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.util.StatusBarUtils;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class BaseActivityStatusBar extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .clearActionBarShadow()
                .init();
    }
}
