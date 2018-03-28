package online.himakeit.qrcodekit.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.ui.common.BaseActivityStatusBar;
import online.himakeit.qrcodekit.util.DialogUtil;
import online.himakeit.qrcodekit.util.FileUtils;
import online.himakeit.qrcodekit.util.LogUtils;
import online.himakeit.qrcodekit.util.QrCodeUtils;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date:2018/3/13-16:47
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：NormalQRActivity
 */
public class NormalQRActivity extends BaseActivityStatusBar implements ColorChooserDialog.ColorCallback {

    @BindView(R.id.tv_normal_qr_background_color)
    TextView mTvBackgroundColor;
    @BindView(R.id.iv_normal_qr_logo)
    ImageView mIvLogo;
    @BindView(R.id.et_normal_qr_input)
    EditText mEtInput;
    @BindView(R.id.iv_normal_qr_img)
    ImageView mIvImg;
    @BindView(R.id.btn_normal_qr_generate)
    Button mBtnGenerate;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    boolean generating = false;
    private WeakReference<Bitmap> qrBitmapWeakReference;
    private WeakReference<Bitmap> logoBitmapWeakReference;
    int colorDark = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_qr);
        ButterKnife.bind(this);

        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        acquireStoragePermissions();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void acquireStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qrBitmapWeakReference != null) {
            qrBitmapWeakReference.get().recycle();
        }
        if (logoBitmapWeakReference != null) {
            logoBitmapWeakReference.get().recycle();
        }
    }

    private void initToolBar() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("普通模式");
    }

    @OnClick({R.id.btn_normal_qr_generate, R.id.iv_normal_qr_logo,
            R.id.tv_normal_qr_background_color})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_normal_qr_generate:
                generateQrImg(colorDark);
                break;
            case R.id.iv_normal_qr_logo:
                selectLogoImage();
                break;
            case R.id.tv_normal_qr_background_color:
                showColorChooserDialog(NormalQRActivity.this);
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
        if (requestCode == 0x001 && data.getData() != null) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.absolutePathFromUri(this, uri));
                logoBitmapWeakReference = new WeakReference<Bitmap>(bitmap);
                if (logoBitmapWeakReference != null) {
                    mIvLogo.setImageBitmap(logoBitmapWeakReference.get());
                    Toasts.showShort("Logo image added.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toasts.showShort("Failed to add the logo image.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnLongClick(R.id.iv_normal_qr_img)
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv_normal_qr_img:
                if (qrBitmapWeakReference != null) {

                    DialogUtil.showMyDialog(NormalQRActivity.this, "提示",
                            "确认保存图片？", "确认", "取消",
                            new DialogUtil.OnDialogClickListener() {
                                @Override
                                public void onConfirm() {
                                    FileUtils.saveBitmap(NormalQRActivity.this,Config.QRCODE_NORMAL_TYPE,
                                            qrBitmapWeakReference.get());
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }

                break;
            default:
                break;
        }
        /**
         * 返回false，那么执行完长按事件后，还有执行单击事件。
         如果返回true，只执行长按事件
         */
        return true;
    }

    private void generateQrImg(int colorDark) {

        try {
            if (generating) {
                return;
            }
            generating = true;

            showProgressDialog("正在生成中...");

            QrCodeUtils.generate(mEtInput.getText().length() == 0 ? getResources().getString(R.string.app_name) : mEtInput.getText().toString(),
                    800, 0, 1.0f, colorDark, Color.WHITE, null,
                    false, true, false, 0, false,
                    logoBitmapWeakReference == null ? null : logoBitmapWeakReference.get(),
                    10, 8, 10, new AwesomeQRCode.Callback() {
                        @Override
                        public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                            qrBitmapWeakReference = new WeakReference<Bitmap>(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (qrBitmapWeakReference != null) {
                                        mIvImg.setImageBitmap(qrBitmapWeakReference.get());
                                    }
                                    dissmissProgressDialog();
                                    generating = false;
                                }
                            });
                        }

                        @Override
                        public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                            e.printStackTrace();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dissmissProgressDialog();
                                    generating = false;
                                }
                            });
                        }
                    });
        } catch (Exception e) {
            Toasts.showShort("发生错误，请重试！");
        }
    }

    private void showColorChooserDialog(Context context) {
        ColorChooserDialog colorChooserDialog = new ColorChooserDialog.Builder(context, R.string.select_color)
                /**
                 * 当查看颜色的深浅时，对话框的标题
                 */
                .titleSub(R.string.detail)
                /**
                 * 如果是true，将显示强调面板而不是主面板
                 */
                .accentMode(false)
                /**
                 * 确认按钮
                 */
                .doneButton(R.string.done)
                /**
                 * 取消按钮
                 */
                .cancelButton(R.string.cancel)
                /**
                 * 返回按钮
                 */
                .backButton(R.string.back)
                /**
                 * 定制按钮
                 */
                .customButton(R.string.custom)
                /**
                 * 预设按钮
                 */
                .presetsButton(R.string.presets)
                /**
                 * 可选的预选一个颜色
                 */
                .preselect(colorDark)
                /**
                 * 默认为true，false将禁用更改操作按钮的颜色到当前选择的颜色
                 */
                .dynamicButtonColor(false)
                .allowUserColorInputAlpha(true)
                .allowUserColorInput(true)
                .show(this);
        colorChooserDialog.setCancelable(false);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        LogUtils.e("--->" + selectedColor);
        colorDark = selectedColor;
        mTvBackgroundColor.setBackgroundColor(selectedColor);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }
}
