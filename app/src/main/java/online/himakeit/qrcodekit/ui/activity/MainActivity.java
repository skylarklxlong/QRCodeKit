package online.himakeit.qrcodekit.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.callback.OnRecyclerItemClickListener;
import online.himakeit.qrcodekit.config.Config;
import online.himakeit.qrcodekit.ui.adapter.MainRecyclerViewAdapter;
import online.himakeit.qrcodekit.ui.common.BaseActivity;
import online.himakeit.qrcodekit.util.DialogUtil;
import online.himakeit.qrcodekit.util.FileUtils;
import online.himakeit.qrcodekit.util.ShareUtil;

/**
 * @author：LiXueLong
 * @date:2018/3/9-17:17
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：MainActivity
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_main)
    RecyclerView mRecyclerView;

    MaterialDialog mMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<String> mItemDatas = new ArrayList<>();
        mItemDatas.add("扫一扫");
        mItemDatas.add("普遍模式");
        mItemDatas.add("高级模式");
        mItemDatas.add("二维码百科");
        mItemDatas.add("淘宝优惠券");

        final ArrayList<String> mTitleDatas = mItemDatas;
        MainRecyclerViewAdapter mRecyclerAdapter = new MainRecyclerViewAdapter(this, mItemDatas);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setmOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = mTitleDatas.get(position);
                Log.e(TAG, "onItemClick: " + position + title);
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ScanQRCodeActivity.class));
//                        Toasts.showShort("正在开发中...");
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, NormalQRActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AwesomeQRActivity.class));
                        break;
                    case 3:
//                        startActivity(WebActivity.newIntent(MainActivity.this,"file:///android_asset/qrcode_baike.html","二维码百科"));
                        startActivity(WebActivity.newIntent(MainActivity.this, "https://baike.baidu.com/item/%E4%BA%8C%E7%BB%B4%E7%A0%81", "二维码百科"));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, TaoBaoActivity.class));
//                        Toasts.showShort("正在开发中...");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_action_info:
                showAppInfoDialog();
                return true;
            case R.id.menu_action_setting:
                startActivity(WebActivity.newIntent(MainActivity.this, "http://himakeit.online/", "XueLong's Blog"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showAppInfoDialog() {
        mMaterialDialog = new MaterialDialog.Builder(MainActivity.this)
                .iconRes(R.mipmap.ic_launcher)
                .title(R.string.app_name)
                .customView(R.layout.dialog_app_info, true)
                .canceledOnTouchOutside(true)
                .cancelable(true)
                .show();

        View customView = mMaterialDialog.getCustomView();
        if (customView != null) {
            TextView mTvZfPay = customView.findViewById(R.id.tv_app_info_zfb_pay);
            TextView mTvWxPay = customView.findViewById(R.id.tv_app_info_wx_pay);
            TextView mTvMarket = customView.findViewById(R.id.tv_app_info_to_market);
            TextView mTvWeibo = customView.findViewById(R.id.tv_app_info_to_weibo);

            setTextUnderline(mTvZfPay);
            setTextUnderline(mTvWxPay);
            setTextUnderline(mTvWeibo);

            mTvZfPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showPayDialog(MainActivity.this, 0, new DialogUtil.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            FileUtils.savePayBitmap("alipay", BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.alipay));
                        }

                        @Override
                        public void onCancel() {
                            FileUtils.savePayBitmap("alipay", BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.alipay));
                            ShareUtil.shareImage(MainActivity.this, Uri.fromFile(FileUtils.getPayFile("alipay")), "支付宝扫一扫打赏");
                        }
                    });
                }
            });
            mTvWxPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showPayDialog(MainActivity.this, 1, new DialogUtil.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            FileUtils.savePayBitmap("wxpay", BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.wxpay));
                        }

                        @Override
                        public void onCancel() {
                            FileUtils.savePayBitmap("wxpay", BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.wxpay));
                            ShareUtil.shareImage(MainActivity.this, Uri.fromFile(FileUtils.getPayFile("wxpay")), "微信扫一扫打赏");

                        }
                    });
                }
            });
            mTvMarket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtil.goToMarket(MainActivity.this, MainActivity.this.getPackageName());
                }
            });
            mTvWeibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtil.shareWeb(MainActivity.this, Config.WEIBO_URL);
                }
            });

        } else {
            throw new IllegalArgumentException("customview must not be null");
        }
    }

    private void setTextUnderline(TextView textView) {
        if (textView != null) {
            textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            textView.getPaint().setAntiAlias(true);
        } else {
            throw new IllegalArgumentException("textview must not be null");
        }
    }
}
