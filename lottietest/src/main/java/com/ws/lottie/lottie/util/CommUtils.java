package com.ws.lottie.lottie.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

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
}
