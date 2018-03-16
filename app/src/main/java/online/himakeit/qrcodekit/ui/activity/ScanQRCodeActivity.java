package online.himakeit.qrcodekit.ui.activity;

import android.os.Bundle;

import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.ui.common.BaseActivityStatusBar;

/**
 * @author：LiXueLong
 * @date:2018/3/15-8:09
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：QrCodeEncyclopediaActivity
 */
public class ScanQRCodeActivity extends BaseActivityStatusBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
    }
}
