package online.himakeit.qrcodekit.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.callback.OnRecyclerItemClickListener;
import online.himakeit.qrcodekit.ui.adapter.MainRecyclerViewAdapter;
import online.himakeit.qrcodekit.ui.common.BaseActivity;

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
        mItemDatas.add("普遍二维码");
        mItemDatas.add("LOGO二维码");
        mItemDatas.add("完美二维码");
        mItemDatas.add("二维码百科");
        mItemDatas.add("淘宝优惠券");
        mItemDatas.add("其他");

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
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showAppInfoDialog() {
        mMaterialDialog = new MaterialDialog.Builder(MainActivity.this)
                .iconRes(R.mipmap.skylark)
                .title(R.string.app_name)
                .customView(R.layout.dialog_app_info, false)
                .canceledOnTouchOutside(true)
                .cancelable(true)
                .show();

        View customView = mMaterialDialog.getCustomView();
    }
}
