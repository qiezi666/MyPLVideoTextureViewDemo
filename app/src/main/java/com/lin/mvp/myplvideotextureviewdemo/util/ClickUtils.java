package com.lin.mvp.myplvideotextureviewdemo.util;

import android.os.SystemClock;
import android.util.Log;

import com.lin.mvp.myplvideotextureviewdemo.BuildConfig;


/**
 * ClassName: ClickUtils<p>
 * Author:oubowu<p>
 * Fuction: 快速点击处理类<p>
 * CreateDate:2016/2/14 13:15<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class ClickUtils {

    private static final String TAG = ClickUtils.class.getSimpleName();
    private static final String BLANK_LOG = "\t";
    private static boolean sIsDebug = BuildConfig.DEBUG;
    private static long sLastClickTime = 0L;

    /**
     * 用于处理频繁点击问题, 如果两次点击小于500毫秒则不予以响应
     *
     * @return true:是连续的快速点击
     */
    public static boolean isFastDoubleClick() {
        long nowTime = SystemClock.elapsedRealtime();//从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）

        if (sIsDebug){
            Log.d(TAG,"nowTime:" + nowTime);
            Log.d(TAG, "lastClickTime:" + sLastClickTime);
            Log.d(TAG,"时间间隔:"+(nowTime - sLastClickTime));
        }
        if ((nowTime - sLastClickTime) < 250) {

            if (sIsDebug){
                Log.d(TAG,"快速点击");
                Log.d(TAG, BLANK_LOG);
            }
            return true;
        } else {
            sLastClickTime = nowTime;

            if (sIsDebug){
                Log.d(TAG,"lastClickTime:" + sLastClickTime);
                Log.d(TAG,"不是快速点击");
                Log.d(TAG,BLANK_LOG);
            }
            return false;
        }
    }
}
