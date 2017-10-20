package com.awen.gesturelib.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.awen.gesturelib.bean.GesturePoint;
import com.awen.gesturelib.callback.GesturePasswordCallback;
import com.awen.gesturelib.model.ResourceModel;
import com.awen.gesturelib.model.ScreenSizeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势背景View
 * 正常状态下显示
 */
public class GestureBackgroundView extends ViewGroup {

    private Context context;
    private int displayWidth;
    private int imgWidth;            //点的宽度
    private int imgHeight;           //点的高度
    private int gestrueViewWidth;    //手势View的宽度
    private int gestrueViewHeight;    //手势View的高度
    private int centerAdjustValue;   //居中调整变量值
    private int spaceValue;          //上下左右间距
    private List<GesturePoint> list; //声明一个集合用来封装坐标集合
    private GestureDrawView gestureDrawView;
    private ScreenSizeModel screenSizeModel;

    public GestureBackgroundView(Context context,int width,boolean isCenter,int marginLeft,GesturePasswordCallback callBack) {
        super(context);
        screenSizeModel = new ScreenSizeModel(context);
        displayWidth = screenSizeModel.getScreenWidth();
        imgWidth = width;
        imgHeight = imgWidth;
        spaceValue = imgWidth/5;//通过数据多次测试,上下左右之间的间距为点的五分之一比较适合
        gestrueViewWidth = 3*imgWidth + 2*spaceValue;
        gestrueViewHeight = gestrueViewWidth;
        if(isCenter){
            centerAdjustValue = (displayWidth - gestrueViewWidth)/2;
        }else{
            if(gestrueViewWidth + marginLeft >= displayWidth){
                centerAdjustValue = displayWidth - gestrueViewWidth;
            }else{
                centerAdjustValue = marginLeft;
            }
        }
        list = new ArrayList<>();
        this.context = context;
        addNineChild();
        gestureDrawView = new GestureDrawView(context, list, callBack);
        setDrawingCacheEnabled(true);

    }

    /**
     * 向GroupView添加9个ImageView,并记录9个点的位置
     */
    private void addNineChild() {
        for (int i = 0; i < 9; i++) {
            int[] processData = processPoint(i);
            ImageView image = new ImageView(context);
            LayoutParams params = new LayoutParams(imgWidth,imgWidth);
            image.setLayoutParams(params);
            image.setBackgroundResource(ResourceModel.getInstance().getNormalResId());
            addView(image);
            GesturePoint p = new GesturePoint(processData[0] + centerAdjustValue, processData[1] + centerAdjustValue,
                    processData[2], processData[3], image, i + 1);
            this.list.add(p);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * 9 个背景ImageView的位置布局
         */
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            GesturePoint p = list.get(i);
            v.layout(p.getLeftX(), p.getTopY(), p.getRightX(),p.getBottomY());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 点位置数据处理
     * @param i
     * @return
     */
    private int[] processPoint(int i){
        int[] processData = new int[4];
        int row = i / 3;    // 第几行
        int col = i % 3;    // 第几列
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        switch (col){
            case 0:
                left = col * imgWidth;
                right = col * imgWidth + imgWidth;
                break;
            case 1:
                left = col * imgWidth + spaceValue;
                right = col * imgWidth + imgWidth + spaceValue;
                break;
            case 2:
                left = col * imgWidth + spaceValue*col;
                right = col * imgWidth + imgWidth + spaceValue*col;
                break;
        }
        switch (row){
            case 0:
                top = row * imgHeight;
                bottom = row * imgHeight + imgHeight ;
                break;
            case 1:
                top = row * imgHeight + spaceValue;
                bottom = row * imgHeight + imgHeight + spaceValue;
                break;
            case 2:
                top = row * imgHeight + spaceValue*row;
                bottom = row * imgHeight + imgHeight  + spaceValue*row;
                break;
        }
        processData[0] = left;
        processData[1] = right;
        processData[2] = top;
        processData[3] = bottom;
        return processData;
    }

    /**
     * 
     * @param parent
     */
    public void addGestureChildView(ViewGroup parent) {
        LayoutParams layoutParams = new LayoutParams(gestrueViewWidth+centerAdjustValue, gestrueViewHeight);
        this.setLayoutParams(layoutParams);
        gestureDrawView.setLayoutParams(layoutParams);
        parent.addView(gestureDrawView);
        parent.addView(this);
    }

    /**
     * 清除绘制状态
     * @param delayTime 延迟时间
     */
    public void clearDrawStatus(long delayTime) {
        gestureDrawView.clearDrawStatus(delayTime);
    }
}
