package online.himakeit.qrcodekit.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.util.FileUtils;
import online.himakeit.qrcodekit.util.Toasts;

/**
 * @author：LiXueLong
 * @date:2018/3/23-15:25
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：OldPosterActivity
 */
public class TripleSendActivity extends AppCompatActivity {
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

    private static final int REQUEST_CODE_SELECT_IMAGE_ONE = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_TWO = 1 << 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_THREE = 1 << 2;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1 << 4;
    private static final int REQUEST_CODE_CUTE_IMAGE = 1 << 5;

    private String mPath1;
    private String mPath2;
    private String mPath3;
    private String mSavePath;
    private String mTempPath;
    private File mCutePhotoFile;
    private File mCurrentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triple_send);

        ButterKnife.bind(this);

        initToolBar();

        mSavePath = FileUtils.getPublicContainer(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        mTempPath = FileUtils.getExternalCacheDir(this);
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
                        Glide.with(TripleSendActivity.this)
                                .load(mCutePhotoFile.getAbsolutePath())
                                .into(mIvImg1);
                    } else if (REQUEST_CODE_SELECT_IMAGE_TWO == pictureCode) {
                        Glide.with(TripleSendActivity.this)
                                .load(mCutePhotoFile.getAbsolutePath())
                                .into(mIvImg2);
                    } else if (REQUEST_CODE_SELECT_IMAGE_THREE == pictureCode) {
                        Glide.with(TripleSendActivity.this)
                                .load(mCutePhotoFile.getAbsolutePath())
                                .into(mIvImg3);
                    }
                }
            }
        }
    }

    private void generateImg() {
        Toasts.showShort("功能正在开发中。。。");
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
