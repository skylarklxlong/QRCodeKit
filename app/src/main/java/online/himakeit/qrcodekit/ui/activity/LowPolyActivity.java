package online.himakeit.qrcodekit.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzhoujay.lowpoly.LowPoly;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

/**
 * @author：LiXueLong
 * @date:2018/3/23-15:24
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：LowPolyActivity
 */
public class LowPolyActivity extends BaseActivityStatusBar {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_low_poly_img)
    ImageView mIvImg;
    @BindView(R.id.btn_low_poly_select)
    Button mBtnSelect;

    WeakReference<Bitmap> imgBitmapWeakReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_poly);

        ButterKnife.bind(this);

        initToolBar();
    }

    private void initToolBar() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("多边形图片");
    }

    @OnClick(R.id.btn_low_poly_select)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_low_poly_select:
                selectLogoImage();
                break;
            default:
                break;
        }

    }

    @OnLongClick(R.id.iv_low_poly_img)
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv_low_poly_img:
                if (imgBitmapWeakReference != null) {

                    DialogUtil.showMyDialog(LowPolyActivity.this, "提示",
                            "确认保存图片？", "确认", "取消",
                            new DialogUtil.OnDialogClickListener() {
                                @Override
                                public void onConfirm() {
                                    FileUtils.saveBitmap(Config.QRCODE_LOWPOLY_TYPE,
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
        /**
         * 返回false，那么执行完长按事件后，还有执行单击事件。
         如果返回true，只执行长按事件
         */
        return true;
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
                showProgressDialog("正在生成中。。。");
                final Uri uri = data.getData();

                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(uri);
                            return LowPoly.generate(inputStream, null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            dissmissProgressDialog();
                            imgBitmapWeakReference = new WeakReference<Bitmap>(bitmap);
                            if (imgBitmapWeakReference != null) {
                                mIvImg.setImageBitmap(imgBitmapWeakReference.get());
                            }
                        } else {
                            Toasts.showShort("生成失败");
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
