package online.himakeit.qrcodekit.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bigkoo.svprogresshud.SVProgressHUD;

import online.himakeit.qrcodekit.R;

/**
 * @author：LiXueLong
 * @date：2018/3/29
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public abstract class TestBaseActivity extends AppCompatActivity {

    protected CoordinatorLayout mRootView;
    protected Toolbar mToolbar;

    private InputMethodManager manager;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContentView() != 0) {
            setContentView(getContentView());
        }

        initData();
        initView(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initView(Bundle savedInstanceState) {
        mRootView = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setToolbarTitle(@StringRes int stringId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stringId);
        }
    }

    protected void setToolbarSubTitle(String subTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setToolbarSubTitle(@StringRes int stringId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(stringId);
        }
    }

    protected void setToolbarBackEnable() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showProgress(@StringRes int stringId) {
        showProgress(getString(stringId));
    }

    protected void showProgress(String content) {
        if (svProgressHUD == null) {
            svProgressHUD = new SVProgressHUD(this);
        }
        hideProgress();
        svProgressHUD.showWithStatus(content, SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    protected void hideProgress() {
        if (svProgressHUD != null && svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }

    protected void showSnackbar(String content) {
        if (mRootView != null) {
            Snackbar.make(mRootView, content, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showSnackbar(@StringRes int contentId) {
        showSnackbar(getString(contentId));
    }

    protected void showSnackbar(@StringRes int btnTextId, @StringRes int contentId, View.OnClickListener click) {
        if (mRootView != null) {
            Snackbar.make(mRootView, contentId, Snackbar.LENGTH_LONG)
                    .setAction(btnTextId, click)
                    .show();
        }
    }

    protected void hideKeyboard() {
        final View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            final IBinder windowToken = currentFocus.getWindowToken();
            if (windowToken != null && manager != null) {
                manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @LayoutRes
    abstract protected int getContentView();

}
