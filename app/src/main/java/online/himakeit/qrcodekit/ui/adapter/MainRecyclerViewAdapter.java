package online.himakeit.qrcodekit.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.himakeit.qrcodekit.R;
import online.himakeit.qrcodekit.callback.OnRecyclerItemClickListener;

/**
 * @author：LiXueLong
 * @date：2018/3/10
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<String> mDatas;
    LayoutInflater mLayoutInflater;

    public MainRecyclerViewAdapter(Context mContext, ArrayList<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_main_recycler_view, parent, false);
        return new MainItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MainItemViewHolder) {
            MainItemViewHolder viewHolder = (MainItemViewHolder) holder;

            if (mOnRecyclerItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnRecyclerItemClickListener.onItemClick(v, position);
                    }
                });
            }

            String title = mDatas.get(position);
            viewHolder.mTvTitle.setText(title);

            if ("扫一扫".equals(title)) {
                viewHolder.mIvImg.setImageResource(R.drawable.icon_scan_72);
            } else if ("普遍模式".equals(title)) {
                viewHolder.mIvImg.setImageResource(R.drawable.icon_scan_72);
            } else if ("高级模式".equals(title)) {
                viewHolder.mIvImg.setImageResource(R.drawable.icon_scan_72);
            } else if ("二维码百科".equals(title)) {
                viewHolder.mIvImg.setImageResource(R.drawable.icon_scan_72);
            } else if ("淘宝优惠券".equals(title)) {
                viewHolder.mIvImg.setImageResource(R.drawable.icon_scan_72);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MainItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_main_recycler_img)
        ImageView mIvImg;
        @BindView(R.id.tv_item_main_recycler_title)
        TextView mTvTitle;

        public MainItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public void setmOnRecyclerItemClickListener(OnRecyclerItemClickListener mOnRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = mOnRecyclerItemClickListener;
    }
}
