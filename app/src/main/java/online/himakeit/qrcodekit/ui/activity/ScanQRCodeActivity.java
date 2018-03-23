package online.himakeit.qrcodekit.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.ui.common.BaseActivityStatusBar;
import online.himakeit.qrcodekit.util.DialogUtil;
import online.himakeit.qrcodekit.util.FileUtils;
import online.himakeit.qrcodekit.util.TextStrUtils;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date:2018/3/15-8:09
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：QrCodeEncyclopediaActivity
 */
public class ScanQRCodeActivity extends BaseActivityStatusBar implements QRCodeView.Delegate {

    @BindView(R.id.zxing_view_scan_qr_code)
    ZXingView mZxingView;
    @BindView(R.id.ll_scan_qrcode_light)
    LinearLayout mLlLight;
    @BindView(R.id.ll_scan_qrcode_gallery)
    LinearLayout mLlGallery;

    public static boolean isLightOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);
        mZxingView.setDelegate(this);

    }

    @OnClick({R.id.ll_scan_qrcode_light, R.id.ll_scan_qrcode_gallery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_scan_qrcode_gallery:
                selectLogoImage();
                break;
            case R.id.ll_scan_qrcode_light:
                if (!isLightOpen) {
                    mZxingView.openFlashlight();
                    isLightOpen = true;
                } else {
                    mZxingView.closeFlashlight();
                    isLightOpen = false;
                }
                break;
            default:
                break;
        }
    }

    private void selectLogoImage() {
        Intent mIntent;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            mIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            mIntent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        mIntent.setType("image/*");
        startActivityForResult(mIntent, 0x001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0x001 && data.getData() != null) {
            final Uri uri = data.getData();
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    return QRCodeDecoder.syncDecodeQRCode(FileUtils.absolutePathFromUri(ScanQRCodeActivity.this, uri));
                }

                @Override
                protected void onPostExecute(String s) {
                    if (TextUtils.isEmpty(s)) {
                        Toasts.showShort("未发现二维码");
                    } else {
                        showScanResult(s);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        showScanResult(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toasts.showShort("打开相机出错！请检查是否开启权限！");
    }

    private void showScanResult(final String result) {
        DialogUtil.showMyDialog(ScanQRCodeActivity.this, "扫描结果", result,
                "复制", "确定", new DialogUtil.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        TextStrUtils.copyText(result);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mZxingView.startCamera();
        mZxingView.showScanRect();
        mZxingView.startSpotDelay(1000);
    }

    @Override
    protected void onPause() {
        mZxingView.stopSpot();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mZxingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingView.onDestroy();
        super.onDestroy();
    }
}
