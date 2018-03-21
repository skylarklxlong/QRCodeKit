package online.himakeit.qrcodekit.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.ui.common.BaseActivityStatusBar;

/**
 * @author：LiXueLong
 * @date:2018/3/21-10:59
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：TaoBaoActivity
 */
public class TaoBaoActivity extends BaseActivityStatusBar {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_bao);

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
        mTvTitle.setText("淘宝优惠券");
    }
}
