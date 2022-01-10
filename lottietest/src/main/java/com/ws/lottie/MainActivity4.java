package com.ws.lottie;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

//https://airbnb.io/lottie/#/supported-features
//http://airbnb.io/lottie/#/android?id=dynamic-properties
public class MainActivity4 extends AppCompatActivity {

    private LottieAnimationView lottieView;
    private WebView contentWebView;
    private LinearLayout rootView;
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        rootView = findViewById(R.id.root);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        contentWebView = new WebView(this);
        rootView.addView(contentWebView);
//        contentWebView.setVisibility(View.GONE);

        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.addJavascriptInterface(this, "wjj");
        contentWebView.loadUrl("file:///android_asset/show.html");
        contentWebView.setWebChromeClient(new WebChromeClient());

        testAndroidToJS();
    }

    @SuppressLint("JavascriptInterface")
    private void testAndroidToJS() {

//        contentWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                loadUrl();
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                loadUrl();
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
    }

    public void loadUrl() {
        if (Build.VERSION.SDK_INT < 18) {
//            contentWebView.loadUrl("javascript:javacalljs()");
            contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello word'" + ")");
        } else {

            contentWebView.evaluateJavascript("javascript:javacalljswithargs(" + "'hello word'" + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Toast.makeText(MainActivity4.this, value, Toast.LENGTH_LONG).show();
                }
            });

            contentWebView.evaluateJavascript("javascript:javacalljs()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Toast.makeText(MainActivity4.this, value, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @JavascriptInterface
    public void startFunction() {
        Toast.makeText(this, "js 调用了Android", Toast.LENGTH_LONG).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(tv.getText() + "\n js 调用Android");
            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(tv.getText() + "\n js 调用Android传递参数：" + str);
            }
        });
    }


}


