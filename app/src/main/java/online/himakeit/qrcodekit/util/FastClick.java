package online.himakeit.qrcodekit.util;

/**
 * @author：LiXueLong
 * @date：2018/3/29
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des:防止快速点击工具
 */
public class FastClick {
    private static long lastClickTime;
    private static long exitClickTime;

    /**
     * 快速点击控制
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 退出点击控制
     *
     * @return
     */
    public synchronized static boolean isExitClick() {
        long time = System.currentTimeMillis();
        if (time - exitClickTime < 2000) {
            return true;
        }
        exitClickTime = time;
        return false;
    }
}
