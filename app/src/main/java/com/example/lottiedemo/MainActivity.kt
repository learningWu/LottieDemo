package com.example.lottiedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //声明控件
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.animation_view);
        //设置动画文件
        lottieAnimationView.setAnimation("lottie_cat.json");
        //是否循环执行
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE;
        //执行动画
        lottieAnimationView.playAnimation();
    }
}