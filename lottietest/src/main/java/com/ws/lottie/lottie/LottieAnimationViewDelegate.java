package com.ws.lottie.lottie;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by wangshun3 on 2021/7/28
 * Des :
 */
abstract class LottieAnimationViewDelegate extends LottieAnimationView {
    private static final String TAG = LottieAnimationViewDelegate.class.getSimpleName();

    public LottieAnimationViewDelegate(Context context) {
        this(context, null);
    }

    public LottieAnimationViewDelegate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LottieAnimationViewDelegate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取触摸点的坐标 x, y
            float x = event.getX();
            float y = event.getY();
            // 目标点的坐标
            float dst[] = new float[2];
            // 获取到ImageView的matrix
            Matrix imageMatrix = getImageMatrix();
            // 创建一个逆矩阵
            Matrix inverseMatrix = new Matrix();
            // 求逆，逆矩阵被赋值
            imageMatrix.invert(inverseMatrix);
            // 通过逆矩阵映射得到目标点 dst 的值
            inverseMatrix.mapPoints(dst, new float[]{x, y});
            float dstX = dst[0];
            float dstY = dst[1];
            onTouchPointEvent(dstX, dstY);
        }
        return super.onTouchEvent(event);
    }


    public abstract void onTouchPointEvent(float x, float y);

}
