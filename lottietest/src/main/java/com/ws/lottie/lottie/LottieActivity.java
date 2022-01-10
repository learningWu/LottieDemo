package com.ws.lottie.lottie;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.ws.lottie.R;

// http://storage.360buyimg.com/ljd-source/ljd_anim/js/bean.zip
public class LottieActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        RelativeLayout rootView = findViewById(R.id.root_view);

        LottieView lottieView = new LottieView(this);

//        lottieView.setRawRes(R.raw.shaizi);
//        lottieView.setJsSourceFromLocal("file:///android_asset/shaizi.html",false);

        lottieView.setRawRes(R.raw.bean);
        lottieView.setJsSourceFromLocal("file:///android_asset/bean.html",true);

        lottieView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(800, 800);
        lottieView.setLayoutParams(lp);
        rootView.addView(lottieView);
    }
}
