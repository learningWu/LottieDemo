package com.airbnb.lottie.model.layer

import android.app.ActionBar
import android.graphics.*
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.R
import com.airbnb.lottie.utils.CommUtils
import com.airbnb.lottie.utils.DateUtils
import com.airbnb.lottie.utils.ICountdown
import com.airbnb.lottie.utils.MyCountDownTimer

class DynamicLayoutLayer(lottieDrawable: LottieDrawable, layerModel: Layer) :
    BaseLayer(lottieDrawable, layerModel) {
    private lateinit var replaceView: TextView

    override fun drawLayer(canvas: Canvas, parentMatrix: Matrix, parentAlpha: Int) {
        Log.d("hhh", "DynamicLayoutLayer")
        if (!this::replaceView.isInitialized) {
            replaceView = getReplaceView()
        }

        canvas.save()

        canvas.concat(parentMatrix)
        replaceView.draw(canvas)

        canvas.restore()
    }

    private fun getReplaceView(): TextView {
        val view = TextView(CommUtils.getApplication()).apply {
            setBackgroundColor(Color.BLACK)
            setTextColor(Color.RED)
            textSize = 24f
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        CommUtils.layoutView(view, 400, 400)
        MyCountDownTimer(1000).start(1642745090004L, object : ICountdown {
            override fun onTick(millisUntilFinished: Long) {
                if (this@DynamicLayoutLayer::replaceView.isInitialized) {
                    replaceView.text =
                        DateUtils.convertToString(millisUntilFinished, DateUtils.FORMAT_HH_MM_SS)
                }
            }

            override fun onFinish() {

            }

            override fun onStart() {

            }
        })
        return view
    }
}