package com.awen.gesturelib.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;


public class ScreenSizeModel {

    private Context mContext;
    private DisplayMetrics displayMetrics;

    public ScreenSizeModel(Context mContext) {
        this.mContext = mContext;
        displayMetrics = getDisplayMetrics();
    }

    public  DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public  int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public  int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public  int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics);
    }

    public  int px2dp(int px) {
        float scale = displayMetrics.density;
        return (int) (px / scale + 0.5f);
    }

    public  int px2dp(float px) {
        float scale = displayMetrics.density;
        return (int) (px / scale + 0.5f);
    }

    public  int getScreenWidth() {
        return displayMetrics.widthPixels;
    }

    public  int getScreenHeight() {
        return displayMetrics.heightPixels;
    }


    public  int[] getScreenDispaly() {
        int[] temp = new int[2];
        temp[0] = displayMetrics.widthPixels;
        temp[1] = displayMetrics.heightPixels;
        return temp;
    }

}
