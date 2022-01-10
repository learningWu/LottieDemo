package com.ws.lottie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.model.KeyPath;
import com.ws.lottie.utils.BitmapUtil;
import com.ws.lottie.utils.DPIUtil;

import java.util.List;

//https://airbnb.io/lottie/#/supported-features
//http://airbnb.io/lottie/#/android?id=dynamic-properties
public class MainActivity3 extends AppCompatActivity {

    private LottieAnimationView lottieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        lottieView = findViewById(R.id.animation_view);
        lottieView.setAnimation(R.raw.data2);

//        lottieView.setImageAssetDelegate(new ImageAssetDelegate() {
//            @Nullable
//            @Override
//            public Bitmap fetchBitmap(LottieImageAsset asset) {
//                System.out.println("assetID: " + asset.getId() + "-" + asset.getWidth());
//                return null;
//            }
//        });

        lottieView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                List<KeyPath> keyPaths = lottieView.resolveKeyPath(new KeyPath("**"));
                for (KeyPath keyPath : keyPaths) {
                    Log.d("KeyPath", keyPath.toString());
                }
            }
        });

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int densityDpi = displayMetrics.densityDpi;
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = true;
        opts.inDensity = densityDpi;

        lottieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottieView.updateBitmap("7",
                        BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.mipmap.gift,
                        DPIUtil.getWidthByDesignValue750(MainActivity3.this, 36),
                                DPIUtil.getWidthByDesignValue750(MainActivity3.this, 50))
                );

//                lottieView.updateBitmap("7",BitmapFactory.decodeResource(MainActivity3.this.getResources(), R.mipmap.gift,opts));
            }
        });
    }
}


