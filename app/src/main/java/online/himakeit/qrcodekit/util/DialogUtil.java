package online.himakeit.qrcodekit.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import online.himakeit.qrcodekit.R;

/**
 * @author：LiXueLong
 * @date：2018/3/9
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:
 */
public class DialogUtil {

    public static MaterialDialog showMyDialog(final Context context, String title, String content, String positiveBtnText, String negativeBtnText, final OnDialogClickListener onDialogClickListener) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .contentGravity(GravityEnum.START)
                .positiveText(positiveBtnText)
                .negativeText(negativeBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onCancel();
                        }
                    }
                })
                .show();
        materialDialog.setCancelable(false);
        return materialDialog;
    }

    public interface OnDialogClickListener {

        void onConfirm();

        void onCancel();
    }

    public static MaterialDialog showMyListDialog(final Context context, String title, int contents, final OnDialogListCallback onDialogListCallback) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(contents)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        onDialogListCallback.onSelection(dialog, itemView, position, text);
                    }
                }).show();
        return materialDialog;
    }

    public interface OnDialogListCallback {

        void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text);
    }

    /**
     * @param context
     * @param type                  0:ali   1:weixin
     * @param onDialogClickListener
     * @return
     */
    public static MaterialDialog showPayDialog(Context context, int type, final OnDialogClickListener onDialogClickListener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_ali_wx_pay, false)
                .positiveText("保存")
                .negativeText("分享")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onCancel();
                        }
                    }
                })
                .show();

        View customView = dialog.getCustomView();
        if (customView != null) {
            TextView mTvTitle = customView.findViewById(R.id.tv_pay_title);
            ImageView mIvImg = customView.findViewById(R.id.iv_pay_img);

            if (type == 0) {
                mTvTitle.setText("支付宝扫一扫打赏");
                mIvImg.setImageResource(R.mipmap.alipay);
            } else if (type == 1) {
                mTvTitle.setText("微信扫一扫打赏");
                mIvImg.setImageResource(R.mipmap.wxpay);
            }
        }

        return dialog;
    }
}
