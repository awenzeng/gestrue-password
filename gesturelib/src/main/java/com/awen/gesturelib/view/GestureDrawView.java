package com.awen.gesturelib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;


import com.awen.gesturelib.bean.GesturePoint;
import com.awen.gesturelib.callback.GesturePasswordCallback;
import com.awen.gesturelib.model.ResourceModel;
import com.awen.gesturelib.model.ScreenSizeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触摸变化View
 * 现象：划线，点状态变化
 */
public class GestureDrawView extends View {

    private int startX;// 声明起点坐标
    private int startY;

    //第一个点圆心坐标
    private int gestureViewMinX;
    private int gestureViewMinY;
    //最后一个点圆心坐标
    private int gestureViewMaxX;
    private int gestureViewMaxY;
    private Paint paint;                       // 声明画笔
    private Canvas mCanvas;                    // 画布
    private Bitmap bitmap;                     // 位图
    private List<GesturePoint> mList;          // 装有各个view坐标的集合

    private List<Pair<GesturePoint, GesturePoint>> lineList;      // 记录画过的线
    private Map<String, GesturePoint> autoCheckPointMap;          // 自动选中的情况点
    private boolean isDrawEnable = true;                          // 是否允许绘制


    private int[] screenDispaly;                         //屏幕的宽度和高度
    private GesturePoint currentPoint;                   //手指当前在哪个Point内
    private GesturePasswordCallback mCallBack;                    //用户绘图的回调
    private StringBuilder passWordSb;                    //用户当前绘制的图形密码
    private ScreenSizeModel screenSizeModel;

    public GestureDrawView(Context context, List<GesturePoint> list, GesturePasswordCallback callBack) {
        super(context);

        mList = list;
        lineList = new ArrayList<>();
        screenSizeModel = new ScreenSizeModel(context);
        screenDispaly = screenSizeModel.getScreenDispaly();

        //初始化画笔
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ResourceModel.getInstance().getLineSize());
        paint.setColor(ResourceModel.getInstance().getLineColorId());// 设置默认连线颜色
        paint.setAntiAlias(true);

        //第一个与最后一个点圆心坐标,确定边界
        gestureViewMinX = list.get(0).getCenterX();
        gestureViewMinY = list.get(0).getCenterY();
        gestureViewMaxX = list.get(list.size() - 1).getCenterX();
        gestureViewMaxY = list.get(list.size() - 1).getCenterY();


        bitmap = Bitmap.createBitmap(screenDispaly[0], screenDispaly[0], Bitmap.Config.ARGB_8888); // 设置位图的宽高
        mCanvas = new Canvas();
        mCanvas.setBitmap(bitmap);

        mCallBack = callBack;
        passWordSb = new StringBuilder();
        initBetweenPointMap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDrawEnable == false) {
            return true;
        }
        paint.setColor(ResourceModel.getInstance().getLineColorId());// 设置默认连线颜色
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                currentPoint = getTouchPoint(startX, startY);// 判断当前点击的位置是处于哪个点之内
                if (currentPoint != null) {
                    currentPoint.setPointStatus(GesturePoint.POINT_STATUS_CLICK);
                    passWordSb.append(currentPoint.getNum());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                clearAllDrawLine();
                GesturePoint movePoint = getTouchPoint((int) event.getX(), (int) event.getY());
                if(currentPoint == null) {
                    currentPoint = movePoint;
                    currentPoint.setPointStatus(GesturePoint.POINT_STATUS_CLICK);
                    passWordSb.append(currentPoint.getNum());
                }
                if (movePoint == null || currentPoint.equals(movePoint)
                        || movePoint.getPointStatus() == GesturePoint.POINT_STATUS_CLICK) {
                    //点击区域不能超出gestureView的区域
                    if (event.getX() >= gestureViewMinX && event.getX() <= gestureViewMaxX
                            && event.getY() >= gestureViewMinY && event.getY() <= gestureViewMaxY) {
                        mCanvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), event.getX(), event.getY(), paint);
                    }

                } else {

                    mCanvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(), movePoint.getCenterX(), movePoint.getCenterY(), paint);
                    movePoint.setPointStatus(GesturePoint.POINT_STATUS_CLICK);

                    // 判断是否中间点需要选中
                    GesturePoint betweenPoint = getBetweenPoint(currentPoint, movePoint);

                    //连接中间点，改变中间点状态
                    if (betweenPoint != null && betweenPoint.getPointStatus() != GesturePoint.POINT_STATUS_CLICK) {

                        Pair<GesturePoint, GesturePoint> pair1 = new Pair<>(currentPoint, betweenPoint);
                        lineList.add(pair1);
                        passWordSb.append(betweenPoint.getNum());

                        Pair<GesturePoint, GesturePoint> pair2 = new Pair<>(betweenPoint, movePoint);
                        lineList.add(pair2);
                        passWordSb.append(movePoint.getNum());

                        betweenPoint.setPointStatus(GesturePoint.POINT_STATUS_CLICK);// 设置中间点选中
                        currentPoint = movePoint;

                    } else {
                        Pair<GesturePoint, GesturePoint> pair = new Pair<>(currentPoint, movePoint);
                        lineList.add(pair);
                        passWordSb.append(movePoint.getNum());
                        currentPoint = movePoint;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (TextUtils.isEmpty(passWordSb.toString())) {
                    break;
                }
                mCallBack.onGesturePassword(passWordSb.toString());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化可能的中间点集合map
     */
    private void initBetweenPointMap() {
        autoCheckPointMap = new HashMap<>();
        autoCheckPointMap.put("1,3", getGesturePointByNum(2));
        autoCheckPointMap.put("1,7", getGesturePointByNum(4));
        autoCheckPointMap.put("1,9", getGesturePointByNum(5));
        autoCheckPointMap.put("2,8", getGesturePointByNum(5));
        autoCheckPointMap.put("3,7", getGesturePointByNum(5));
        autoCheckPointMap.put("3,9", getGesturePointByNum(6));
        autoCheckPointMap.put("4,6", getGesturePointByNum(5));
        autoCheckPointMap.put("7,9", getGesturePointByNum(8));
    }

    private GesturePoint getGesturePointByNum(int num) {
        for (GesturePoint point : mList) {
            if (point.getNum() == num) {
                return point;
            }
        }
        return null;
    }


    /**
     * 获取中间点
     * @param pointStart
     * @param pointEnd
     * @return
     */
    private GesturePoint getBetweenPoint(GesturePoint pointStart, GesturePoint pointEnd) {
        int startNum = pointStart.getNum();
        int endNum = pointEnd.getNum();
        String key;
        if (startNum < endNum) {
            key = startNum + "," + endNum;
        } else {
            key = endNum + "," + startNum;
        }
        return autoCheckPointMap.get(key);
    }

    /**
     * 获取到触摸点
     *
     * @param x
     * @param y
     * @return 返回null, 没有触摸到点
     */
    private GesturePoint getTouchPoint(int x, int y) {
        for (GesturePoint point : mList) {
            int leftX = point.getLeftX();
            int rightX = point.getRightX();
            if (!(x >= leftX && x < rightX)) {//点x坐标范围
                continue;
            }
            int topY = point.getTopY();
            int bottomY = point.getBottomY();
            if (!(y >= topY && y < bottomY)) {//点Y坐标范围
                continue;
            }
            return point;
        }
        return null;
    }


    /**
     * 清除绘制状态
     *
     * @param delayTime 延迟时间
     */
    public void clearDrawStatus(long delayTime) {
        if (delayTime > 0) {
            isDrawEnable = false;
            drawErrorPathTip(); // 绘制红色提示路线
        }
        new Handler().postDelayed(new clearStatusRunnable(), delayTime);
    }

    /**
     * 清除绘制状态的线程
     */
    final class clearStatusRunnable implements Runnable {
        public void run() {
            passWordSb = new StringBuilder();
            lineList.clear();
            clearAllDrawLine();
            for (GesturePoint p : mList) {
                p.setPointStatus(GesturePoint.POINT_STATUS_NORMAL);
            }
            invalidate();
            isDrawEnable = true;
        }
    }

    /**
     * 清理所有触摸划线
     */
    private void clearAllDrawLine() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);
        }
    }


    /**
     * 绘制错误路径提示线路
     */
    private void drawErrorPathTip() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(ResourceModel.getInstance().getErroLineColorId());
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            pair.first.setPointStatus(GesturePoint.POINT_STATUS_WRONG);
            pair.second.setPointStatus(GesturePoint.POINT_STATUS_WRONG);
            mCanvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(), pair.second.getCenterX(), pair.second.getCenterY(), paint);
        }
        invalidate();
    }
}
