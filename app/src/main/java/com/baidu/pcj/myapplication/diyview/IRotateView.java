package com.baidu.pcj.myapplication.diyview;

import android.view.animation.Animation;

/**
 * Created by puchunjie .
 */

public interface IRotateView {
    /**
     * 不使用动画设置方向
     *
     * @param orien
     */
    public void requestOrientation(Orientation orien);

    /**
     * 请求方向改变
     *
     * @param orien
     * @param isAnimated 是否启用动画
     */
    public void requestOrientation(Orientation orien, boolean isAnimated);

    /**
     * 设置旋转角度
     *
     * @param angle
     */
    public void setAngle(int angle);

    /**
     * 获取旋转角度
     *
     * @return
     */
    public int getAngle();

    /**
     * View 方法
     * 清除动画
     */
    public void clearAnimation();

    /**
     * View 方法
     * 启动动画
     *
     * @param animation
     */
    public void startAnimation(Animation animation);

    /**
     * View 方法
     *
     * @return
     */
    public int getVisibility();

    /**
     * View 方法
     *
     * @return
     */
    public boolean isShown();
}
