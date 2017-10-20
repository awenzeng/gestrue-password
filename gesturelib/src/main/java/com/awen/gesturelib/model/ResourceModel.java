package com.awen.gesturelib.model;

/**
 * Created by Administrator on 2017/10/18.
 */

public class ResourceModel {

    private static ResourceModel mResourceModel;
    private int normalResId;     //默认背景
    private int clickResId;      //点击背景
    private int erroResId;       //错误背景
    private int lineColorId;     //线的颜色
    private int erroLineColorId; //错误线的颜色
    private int lineSize;

    public static ResourceModel getInstance(){
          synchronized (ResourceModel.class){
             if(mResourceModel == null){
                 mResourceModel = new ResourceModel();
             }
          }
          return mResourceModel;
    }

    public int getNormalResId() {
        return normalResId;
    }

    public void setNormalResId(int normalResId) {
        this.normalResId = normalResId;
    }

    public int getClickResId() {
        return clickResId;
    }

    public void setClickResId(int clickResId) {
        this.clickResId = clickResId;
    }

    public int getErroResId() {
        return erroResId;
    }

    public void setErroResId(int erroResId) {
        this.erroResId = erroResId;
    }

    public int getLineColorId() {
        return lineColorId;
    }

    public void setLineColorId(int lineColorId) {
        this.lineColorId = lineColorId;
    }

    public int getErroLineColorId() {
        return erroLineColorId;
    }

    public void setErroLineColorId(int erroLineColorId) {
        this.erroLineColorId = erroLineColorId;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }
}
