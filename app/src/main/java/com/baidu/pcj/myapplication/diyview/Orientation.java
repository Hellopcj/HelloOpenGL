package com.baidu.pcj.myapplication.diyview;

/**
 * Created by puchunjie .
 */

public enum  Orientation {
    UNKNOWN(-1),
    PORTRAIT(0),            // 屏幕转到竖屏
    PORTRAIT_REVERSE(180),      // 竖屏转到竖屏, 逆时针旋转180度
    LANDSCAPE(90),          // 屏幕转到横屏, 顺时针旋转90度
    LANDSCAPE_REVERSE(-90); // 屏幕转到横屏, 逆时针旋转90度

    /**
     * 手机屏幕的角度
     */
    private int mDegree;

    /**
     * 构造函数，枚举类型只能为私有
     */
    Orientation(int degree) {
        mDegree = degree;
    }

    /**
     * 初始化Orientation
     *
     * @param degree
     *
     * @return
     */
    public static Orientation valueOf(int degree) {
        if (degree == PORTRAIT.getDegree()) {
            return PORTRAIT;
        } else if (degree == PORTRAIT_REVERSE.getDegree()) {
            return PORTRAIT_REVERSE;
        } else if (degree == LANDSCAPE.getDegree()) {
            return LANDSCAPE;
        } else if (degree == LANDSCAPE_REVERSE.getDegree()) {
            return LANDSCAPE_REVERSE;
        }
        return UNKNOWN;
    }

    /**
     * 获取角度
     *
     * @return
     */
    public int getDegree() {
        return mDegree;
    }

    @Override
    public String toString() {
        return String.valueOf(mDegree);
    }
}
