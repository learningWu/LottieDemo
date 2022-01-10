package com.ws.lottie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieImageAsset;

public class JDDynamicLottieView extends View {

    private LottieAnimationView animationView;

    public JDDynamicLottieView(Context context) {
        this(context, null);
    }

    public JDDynamicLottieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JDDynamicLottieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLottieView();
    }


    private void initLottieView() {
        animationView = new LottieAnimationView(getContext());
        animationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        animationView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        animationView.setAnimation(R.raw.bean);
        animationView.setScale(0.4f);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int densityDpi = displayMetrics.densityDpi;
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = true;
        opts.inDensity = densityDpi;

        animationView.setImageAssetDelegate(new ImageAssetDelegate() {
            @Nullable
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
                System.out.println("ws asset id: " + asset.getId());
                String assetId = asset.getId();
                int res = 0;
                if ("image_0".equals(assetId)) {
                    res = R.mipmap.hat;
                } else if ("image_1".equals(assetId)) {
                    res = R.mipmap.leftarm;
                } else if ("image_2".equals(assetId)) {
                    res = R.mipmap.cloth;
                } else if ("image_3".equals(assetId)) {
                    res = R.mipmap.rightarm;
                } else if ("image_4".equals(assetId)) {
                    res = R.mipmap.beanbody;
                } else if ("image_5".equals(assetId)) {
                    res = R.mipmap.pant;
                }
                if (res == 0) {
                    return null;
                }
                return BitmapFactory.decodeResource(getContext().getResources(), res, opts);
            }
        });


    }


}
