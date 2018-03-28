package online.himakeit.qrcodekit.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.ui.common.BaseActivityStatusBar;
import online.himakeit.qrcodekit.util.FileUtils;
import online.himakeit.qrcodekit.util.QrCodeUtils;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date:2018/3/10-11:41
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：AwesomeQRActivity
 */
public class AwesomeQRActivity extends BaseActivityStatusBar {

    private final int BKG_IMAGE = 822;
    private final int LOGO_IMAGE = 379;

    @BindView(R.id.qrcode)
    ImageView qrCodeImageView;
    @BindView(R.id.colorLight)
    EditText etColorLight;
    @BindView(R.id.colorDark)
    EditText etColorDark;
    @BindView(R.id.contents)
    EditText etContents;
    @BindView(R.id.margin)
    EditText etMargin;
    @BindView(R.id.size)
    EditText etSize;
    @BindView(R.id.dotScale)
    EditText etDotScale;
    @BindView(R.id.logoMargin)
    EditText etLogoMargin;
    @BindView(R.id.logoScale)
    EditText etLogoScale;
    @BindView(R.id.logoRadius)
    EditText etLogoCornerRadius;
    @BindView(R.id.binarizeThreshold)
    EditText etBinarizeThreshold;
    @BindView(R.id.generate)
    Button btGenerate;
    @BindView(R.id.backgroundImage)
    Button btSelectBG;
    @BindView(R.id.removeBackgroundImage)
    Button btRemoveBackgroundImage;
    @BindView(R.id.open)
    Button btOpen;
    @BindView(R.id.removeLogoImage)
    Button btRemoveLogoImage;
    @BindView(R.id.logoImage)
    Button btSelectLogo;
    @BindView(R.id.whiteMargin)
    CheckBox ckbWhiteMargin;
    @BindView(R.id.autoColor)
    CheckBox ckbAutoColor;
    @BindView(R.id.binarize)
    CheckBox ckbBinarize;
    @BindView(R.id.rounded)
    CheckBox ckbRoundedDataDots;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.resultViewContainer)
    ViewGroup resultViewContainer;
    @BindView(R.id.configViewContainer)
    ViewGroup configViewContainer;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private Bitmap backgroundImage = null;
    private boolean generating = false;
    private WeakReference<Bitmap> qrBitmapWeakReference;
    private Bitmap logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awesome_qr);
        ButterKnife.bind(this);
        initToolBar();
        initCheckBox();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qrBitmapWeakReference != null) {
            qrBitmapWeakReference.get().recycle();
        }
    }

    private void initToolBar() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("高级模式");
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

    private void initCheckBox() {
        ckbAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etColorLight.setEnabled(!isChecked);
                etColorDark.setEnabled(!isChecked);
            }
        });

        ckbBinarize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etBinarizeThreshold.setEnabled(isChecked);
            }
        });
    }

    @OnClick({R.id.backgroundImage, R.id.logoImage, R.id.open, R.id.removeBackgroundImage,
            R.id.removeLogoImage, R.id.generate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backgroundImage:
                selectImage(BKG_IMAGE);
                break;
            case R.id.logoImage:
                selectImage(LOGO_IMAGE);
                break;
            case R.id.open:
                if (qrBitmapWeakReference != null) {
                    FileUtils.saveBitmap(Config.QRCODE_AWESOME_TYPE, qrBitmapWeakReference.get());
                }
                break;
            case R.id.removeBackgroundImage:
                backgroundImage = null;
                Toasts.showShort("Background image removed.");
                break;
            case R.id.removeLogoImage:
                logoImage = null;
                Toasts.showShort("Logo image removed.");
                break;
            case R.id.generate:
                generateQrImg();
                break;
            default:
                break;
        }
    }

    private void generateQrImg() {
        try {
            if (generating) {
                return;
            }
            generating = true;

            showProgressDialog("正在生成中...");

            QrCodeUtils.generate(etContents.getText().length() == 0 ? getResources().getString(R.string.app_name) : etContents.getText().toString(),
                    etSize.getText().length() == 0 ? 800 : Integer.parseInt(etSize.getText().toString()),
                    etMargin.getText().length() == 0 ? 20 : Integer.parseInt(etMargin.getText().toString()),
                    etDotScale.getText().length() == 0 ? 0.3f : Float.parseFloat(etDotScale.getText().toString()),
                    ckbAutoColor.isChecked() ? Color.BLACK : Color.parseColor(etColorDark.getText().toString()),
                    ckbAutoColor.isChecked() ? Color.WHITE : Color.parseColor(etColorLight.getText().toString()),
                    backgroundImage,
                    ckbWhiteMargin.isChecked(),
                    ckbAutoColor.isChecked(),
                    ckbBinarize.isChecked(),
                    etBinarizeThreshold.getText().length() == 0 ? 128 : Integer.parseInt(etBinarizeThreshold.getText().toString()),
                    ckbRoundedDataDots.isChecked(),
                    logoImage,
                    etLogoMargin.getText().length() == 0 ? 10 : Integer.parseInt(etLogoMargin.getText().toString()),
                    etLogoCornerRadius.getText().length() == 0 ? 8 : Integer.parseInt(etLogoCornerRadius.getText().toString()),
                    etLogoScale.getText().length() == 0 ? 10 : Float.parseFloat(etLogoScale.getText().toString()),
                    new AwesomeQRCode.Callback() {
                        @Override
                        public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                            qrBitmapWeakReference = new WeakReference<Bitmap>(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dissmissProgressDialog();
                                    if (qrBitmapWeakReference != null) {
                                        qrCodeImageView.setImageBitmap(qrBitmapWeakReference.get());
                                    }
                                    configViewContainer.setVisibility(View.GONE);
                                    resultViewContainer.setVisibility(View.VISIBLE);
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
                                    configViewContainer.setVisibility(View.VISIBLE);
                                    resultViewContainer.setVisibility(View.GONE);
                                    generating = false;
                                }
                            });
                        }
                    }
            );
        } catch (Exception e) {
            Toasts.showShort("Error occurred, please check your configs.");
        }
    }

    private void selectImage(int requestCode) {
        Intent intent;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                if (requestCode == BKG_IMAGE) {
                    backgroundImage = BitmapFactory.decodeFile(FileUtils.absolutePathFromUri(this, imageUri));
                    Toasts.showShort("Background image added.");
                } else if (requestCode == LOGO_IMAGE) {
                    logoImage = BitmapFactory.decodeFile(FileUtils.absolutePathFromUri(this, imageUri));
                    Toasts.showShort("Logo image added.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (requestCode == BKG_IMAGE) {
                    Toasts.showShort("Failed to add the background image.");
                } else if (requestCode == LOGO_IMAGE) {
                    Toasts.showShort("Failed to add the logo image.");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (configViewContainer.getVisibility() != View.VISIBLE) {
                configViewContainer.setVisibility(View.VISIBLE);
                resultViewContainer.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
