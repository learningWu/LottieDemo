package com.ws.lottie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.airbnb.lottie.LottieAnimationView;

public class DynamicLottieView extends LottieAnimationView {

    private Context mContext;
    private WebView webView;
    private BitmapFactory.Options opts;

    public DynamicLottieView(Context context) {
        this(context, null);
    }

    public DynamicLottieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLottieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setDensityDpi();
        initWebView();
    }

    public void setJsUrl(String url){
        if (webView!=null){
            webView.loadUrl("file:///android_asset/show.html");
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        webView = new WebView(mContext);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "JDLottieView");
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void setDensityDpi() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;
        opts = new BitmapFactory.Options();
        opts.inScaled = true;
        opts.inDensity = densityDpi;
    }


}
