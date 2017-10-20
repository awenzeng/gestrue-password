package com.awen.gesturelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.awen.gesturelib.R;
import com.awen.gesturelib.callback.GesturePasswordCallback;
import com.awen.gesturelib.model.ResourceModel;
import com.awen.gesturelib.model.ScreenSizeModel;


/**
 * Created by AwenZeng on 2017/10/18.
 */

public class GesturePasswordView extends FrameLayout implements GesturePasswordCallback {
    private Context mContext;
    private int normalResId;     //默认背景
    private int clickResId;      //点击背景
    private int erroResId;       //错误背景
    private int lineColorId;     //线的颜色
    private int erroLineColorId; //错误线的颜色
    private int imgWidth;        //点的宽度
    private boolean isCenter;    //是否居中
    private int marginLeft;      //左边距
    private GestureBackgroundView gestureBgView;

    private GesturePasswordCallback mCallback;
    private ScreenSizeModel screenSizeModel;


    public GesturePasswordView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public GesturePasswordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GesturePasswordView, 0, 0);
        init(context,ta);
    }

    public GesturePasswordView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GesturePasswordView, defStyleAttr, 0);
        init(context,ta);
    }

    private void init(Context context, TypedArray typedArray){
        mContext = context;
        screenSizeModel = new ScreenSizeModel(context);
        normalResId = typedArray.getResourceId(R.styleable.GesturePasswordView_normal_resId,R.drawable.gesture_normal);
        clickResId = typedArray.getResourceId(R.styleable.GesturePasswordView_click_resId,R.drawable.gesture_active);
        erroResId = typedArray.getResourceId(R.styleable.GesturePasswordView_erro_resId,R.drawable.gesture_erro);
        lineColorId = typedArray.getColor(R.styleable.GesturePasswordView_line_color,getResources().getColor(R.color.color_main));
        erroLineColorId = typedArray.getColor(R.styleable.GesturePasswordView_erro_line_color,getResources().getColor(R.color.color_red));
        imgWidth = typedArray.getDimensionPixelOffset(R.styleable.GesturePasswordView_imgWidth,screenSizeModel.dp2px(73f));
        isCenter = typedArray.getBoolean(R.styleable.GesturePasswordView_centerHorizontal,true);
        marginLeft = typedArray.getDimensionPixelOffset(R.styleable.GesturePasswordView_marginLeft,0);
        ResourceModel.getInstance().setNormalResId(normalResId);
        ResourceModel.getInstance().setClickResId(clickResId);
        ResourceModel.getInstance().setErroResId(erroResId);
        ResourceModel.getInstance().setLineColorId(lineColorId);
        ResourceModel.getInstance().setErroLineColorId(erroLineColorId);
        ResourceModel.getInstance().setLineSize(imgWidth/30);//通过数据多次测试,线的大小为点的宽度的30分之一比较适合。
        gestureBgView = new GestureBackgroundView(mContext,imgWidth,isCenter,marginLeft,this);
        gestureBgView.addGestureChildView(this);

    }

    @Override
    public void onGesturePassword(String password) {
        if(mCallback!=null){
            mCallback.onGesturePassword(password);
        }
    }

    /**
     * 清除绘制状态
     * @param delayTime 延迟时间
     */
    public void clearDrawStatus(long delayTime) {
        gestureBgView.clearDrawStatus(delayTime);
    }

    public void setGesturePasswordCallback(GesturePasswordCallback gesturePasswordCallback) {
        this.mCallback = gesturePasswordCallback;
    }
}
