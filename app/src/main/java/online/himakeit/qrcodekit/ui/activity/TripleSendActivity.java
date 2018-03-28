package online.himakeit.qrcodekit.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
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
import online.himakeit.qrcodekit.util.Toasts;
import online.himakeit.qrcodekit.util.TripleImgUtils;

/**
 * @author：LiXueLong
 * @date:2018/3/23-15:25
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：OldPosterActivity
 */
public class TripleSendActivity extends BaseActivityStatusBar {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_triple_send_title)
    EditText mEtTitle;
    @BindView(R.id.et_triple_send_1)
    EditText mEtMsg1;
    @BindView(R.id.et_triple_send_2)
    EditText mEtMsg2;
    @BindView(R.id.et_triple_send_3)
    EditText mEtMsg3;
    @BindView(R.id.iv_triple_send_1)
    ImageView mIvImg1;
    @BindView(R.id.iv_triple_send_2)
    ImageView mIvImg2;
    @BindView(R.id.iv_triple_send_3)
    ImageView mIvImg3;
    @BindView(R.id.iv_triple_send_img)
    ImageView mIvImg;
    @BindView(R.id.btn_triple_send_generate)
    Button mBtnGenerate;

    WeakReference<Bitmap> imgBitmapWeakReference;

    private static final int REQUEST_CODE_SELECT_IMAGE_ONE = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_TWO = 1 << 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_THREE = 1 << 2;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1 << 4;
    private static final int REQUEST_CODE_CUTE_IMAGE = 1 << 5;

    private String mPath1;
    private String mPath2;
    private String mPath3;
    private String mTempPath;
    private File mCutePhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triple_send);

        ButterKnife.bind(this);

        initToolBar();

        mTempPath = FileUtils.getExternalCacheDir(this);
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

        if (imgBitmapWeakReference != null){
            imgBitmapWeakReference.get().recycle();
        }
    }

    private void initToolBar() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("表情三连");
    }

    @OnClick({R.id.iv_triple_send_1, R.id.iv_triple_send_2, R.id.iv_triple_send_3, R.id.btn_triple_send_generate})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_triple_send_1:
                selectImage(REQUEST_CODE_SELECT_IMAGE_ONE);
                break;
            case R.id.iv_triple_send_2:
                selectImage(REQUEST_CODE_SELECT_IMAGE_TWO);
                break;
            case R.id.iv_triple_send_3:
                selectImage(REQUEST_CODE_SELECT_IMAGE_THREE);
                break;
            case R.id.btn_triple_send_generate:
                generateImg();
                break;
            default:
                break;
        }
    }

    @OnLongClick(R.id.iv_triple_send_img)
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv_triple_send_img:
                if (imgBitmapWeakReference != null) {
                    DialogUtil.showMyDialog(TripleSendActivity.this, "提示",
                            "确认保存图片？", "确认", "取消",
                            new DialogUtil.OnDialogClickListener() {
                                @Override
                                public void onConfirm() {
                                    FileUtils.saveBitmap(Config.QRCODE_TRIPLE_TYPE,
                                            imgBitmapWeakReference.get());
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
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int actionCode = getActionCode(requestCode);
            int pictureCode = getPictureCode(requestCode);
            if (actionCode == REQUEST_CODE_SELECT_IMAGE) {
                if (data != null) {
                    cutPicture(data.getData(), pictureCode);
                }
            } else if (actionCode == REQUEST_CODE_CUTE_IMAGE) {
                if (mCutePhotoFile != null && mCutePhotoFile.exists()) {
                    if (REQUEST_CODE_SELECT_IMAGE_ONE == pictureCode) {
                        mPath1 = mCutePhotoFile.getAbsolutePath();
                        Glide.with(TripleSendActivity.this)
                                .load(mPath1)
                                .into(mIvImg1);
                        mPath2 = mPath1;
                        Glide.with(TripleSendActivity.this)
                                .load(mPath2)
                                .into(mIvImg2);
                        mPath3 = mPath1;
                        Glide.with(TripleSendActivity.this)
                                .load(mPath3)
                                .into(mIvImg3);
                    } else if (REQUEST_CODE_SELECT_IMAGE_TWO == pictureCode) {
                        mPath2 = mCutePhotoFile.getAbsolutePath();
                        Glide.with(TripleSendActivity.this)
                                .load(mPath2)
                                .into(mIvImg2);
                    } else if (REQUEST_CODE_SELECT_IMAGE_THREE == pictureCode) {
                        mPath3 = mCutePhotoFile.getAbsolutePath();
                        Glide.with(TripleSendActivity.this)
                                .load(mPath3)
                                .into(mIvImg3);
                    }
                }
            }
        }
    }

    private void generateImg() {
        final String strTitle = mEtTitle.getText().toString();
        final String strName1 = mEtMsg1.getText().toString();
        final String strName2 = mEtMsg1.getText().toString();
        final String strName3 = mEtMsg1.getText().toString();
        if (TextUtils.isEmpty(strTitle)) {
            Toasts.showShort("请先输入标题");
        } else if (TextUtils.isEmpty(strName1)) {
            Toasts.showShort("请输入图片1文字内容");
        } else if (TextUtils.isEmpty(strName2)) {
            Toasts.showShort("请输入图片2文字内容");
        } else if (TextUtils.isEmpty(strName3)) {
            Toasts.showShort("请输入图片3文字内容");
        } else if (TextUtils.isEmpty(mPath1)) {
            Toasts.showShort("请先选择图片1");
        } else if (TextUtils.isEmpty(mPath2)) {
            Toasts.showShort("请先选择图片2");
        } else if (TextUtils.isEmpty(mPath3)) {
            Toasts.showShort("请先选择图片3");
        } else {
            showProgressDialog("图片处理中...");
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return TripleImgUtils.createExpression(strTitle, mPath1, mPath2, mPath3, strName1, strName2, strName3);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        imgBitmapWeakReference = new WeakReference<Bitmap>(bitmap);
                        if (imgBitmapWeakReference != null) {
                            mIvImg.setImageBitmap(imgBitmapWeakReference.get());
                        }
                    } else {
                        Toasts.showShort("生成失败");
                    }

                    dissmissProgressDialog();
                }
            }.execute();
        }
    }

    private void selectImage(int requestCode) {
        Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(mIntent, requestCode | REQUEST_CODE_SELECT_IMAGE);
    }

    private void cutPicture(Uri uri, int requestCode) {
        mCutePhotoFile = new File(mTempPath, System.currentTimeMillis() + ".jpg");
        Uri outputUri = Uri.fromFile(mCutePhotoFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode | REQUEST_CODE_CUTE_IMAGE);
    }

    private int getPictureCode(int code) {
        if ((REQUEST_CODE_SELECT_IMAGE_ONE & code) != 0) {
            return REQUEST_CODE_SELECT_IMAGE_ONE;
        } else if ((REQUEST_CODE_SELECT_IMAGE_TWO & code) != 0) {
            return REQUEST_CODE_SELECT_IMAGE_TWO;
        } else if ((REQUEST_CODE_SELECT_IMAGE_THREE & code) != 0) {
            return REQUEST_CODE_SELECT_IMAGE_THREE;
        }
        return -1;
    }

    private int getActionCode(int code) {
        if ((REQUEST_CODE_SELECT_IMAGE & code) != 0) {
            return REQUEST_CODE_SELECT_IMAGE;
        } else if ((REQUEST_CODE_CUTE_IMAGE & code) != 0) {
            return REQUEST_CODE_CUTE_IMAGE;
        }
        return -1;
    }
}
