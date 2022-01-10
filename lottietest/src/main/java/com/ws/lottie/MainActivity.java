package com.ws.lottie;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView lottieView;
    private int[] COLORS = {0xff5a5f, 0x008489, 0xa61d55};
    private float[] EXTRA_JUMP = {10f, 20f, 50f};
    private int speed = 1;
    private int colorIndex = 0;
    private int extraJumpIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieView = findViewById(R.id.animation_view);

        lottieView.setAnimation(R.raw.androidwave);

        lottieView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                List<KeyPath> keyPaths = lottieView.resolveKeyPath(new KeyPath("**"));
                for (KeyPath keyPath : keyPaths) {
                    Log.d("KeyPath", keyPath.keysToString());
                }
            }
        });

        setupValueCallbacks();
    }

    public void setupValueCallbacks() {
        lottieView.addValueCallback(new KeyPath("LeftArmWave"), LottieProperty.TIME_REMAP, new SimpleLottieValueCallback<Float>() {
            @Override
            public Float getValue(LottieFrameInfo<Float> frameInfo) {
                return 2 * frameInfo.getOverallProgress();
            }
        });

        KeyPath shirt = new KeyPath("Shirt", "Group 5", "Fill 1");
        KeyPath leftArm = new KeyPath("LeftArmWave", "LeftArm", "Group 6", "Fill 1");
        KeyPath rightArm = new KeyPath("RightArm", "Group 6", "Fill 1");

        lottieView.addValueCallback(shirt, LottieProperty.COLOR, new SimpleLottieValueCallback<Integer>() {
            @Override
            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                return COLORS[0];
            }
        });

        lottieView.addValueCallback(leftArm, LottieProperty.COLOR, new SimpleLottieValueCallback<Integer>() {
            @Override
            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                return COLORS[1];
            }
        });

        lottieView.addValueCallback(rightArm, LottieProperty.COLOR, new SimpleLottieValueCallback<Integer>() {
            @Override
            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                return COLORS[2];
            }
        });

        final PointF point = new PointF();
        lottieView.addValueCallback(new KeyPath("Body"), LottieProperty.TRANSFORM_POSITION, new SimpleLottieValueCallback<PointF>() {
            @Override
            public PointF getValue(LottieFrameInfo<PointF> frameInfo) {

                float startX = frameInfo.getStartValue().x;
                float startY = frameInfo.getStartValue().y;
                float endY = frameInfo.getEndValue().y;

                if (startY > endY) {
                    startY += EXTRA_JUMP[extraJumpIndex];
                } else if (endY > startY) {
                    endY += EXTRA_JUMP[extraJumpIndex];
                }
                point.set(startX, lerp(startY, endY, frameInfo.getInterpolatedKeyframeProgress()));
                return point;
            }
        });
    }

    public float lerp(float a, float b, float percentage) {
        return a + percentage * (b - a);
    }

}
