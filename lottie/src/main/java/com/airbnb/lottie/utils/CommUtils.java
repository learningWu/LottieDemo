package com.airbnb.lottie.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;

public class CommUtils {
    private static Application application;
    private static BitmapFactory.Options options;

    static {
        initDensity();
    }

    private static void initDensity() {
        DisplayMetrics displayMetrics = getApplication().getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;
        options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = densityDpi;
    }

    @SuppressLint("PrivateApi")
    public static Application getApplication() {
        if (application == null) {
            try {
                application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return application;
    }

    public static Bitmap getBitmap(int id) {
        Bitmap bitmap = null;
        if (options != null) {
            bitmap = BitmapFactory.decodeResource(getApplication().getResources(), id, options);
        }
        return bitmap;
    }

    /**
     * 获取已经显示的view的bitmap
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap = null;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        }
        return bitmap;
    }

    /**
     * 获取未显示的view的bitmap
     * @param view
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromView(View view, int width, int height) {
        layoutView(view, width, height);
        return getCacheBitmapFromView(view);
    }

    /**
     * 布局控件
     * @param view
     * @param width
     * @param height
     */
    public static void layoutView(View view, int width, int height) {
        view.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

}
