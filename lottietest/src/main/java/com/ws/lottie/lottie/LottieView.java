package com.ws.lottie.lottie;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.TextDelegate;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.ws.lottie.R;
import com.ws.lottie.lottie.util.CommUtils;

import java.util.List;
import java.util.Map;

/**
 * 1、获取js
 * 2、读取img
 * 3、解析互动信息
 */
public class LottieView extends FrameLayout implements ILottieView {
    private static final String TAG = LottieView.class.getSimpleName();
    private Context mContext;
    private WebView mWebView;
    private LottieAnimationView lottieAnimationView;
    private JsonParser.LottieAnimConfig lottieAnimConfig;
    private int currentPlayFrame;
    private Map<String, String> configMap;
    private Map<String, KeyPathProperty> pathPropertyMap;
    private List<JsonParser.FrameConfig> frameConfigs;

    public LottieView(@NonNull Context context) {
        this(context, null);
    }

    public LottieView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LottieView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initLottieView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setJsSourceFromLocal(String path, boolean needImageAsset) {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "LottieView");
        mWebView.loadUrl(path);

        if (needImageAsset && lottieAnimationView != null) {
            lottieAnimationView.setImageAssetDelegate(new ImageAssetDelegate() {
                @Nullable
                @Override
                public Bitmap fetchBitmap(LottieImageAsset asset) {
                    if (configMap == null) {
                        initAnimConfig();
                        return null;
                    }
                    String assetId = asset.getId();
                    if (configMap != null) {
                        String res = configMap.get(assetId);
                        int resId = getRes(res);
                        return CommUtils.getBitmap(resId);
                    }
                    return null;
                }
            });
        } else {
            if (configMap == null) {
                initAnimConfig();
            }
        }
    }

    private void initAnimConfig() {
        if (mWebView != null) {
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    System.out.println("onPageFinished");
                    callJsMethodByLoadUrl("javascript:getInitAnimConfig()");
                }
            });
        }
    }

    public void setRawRes(int rawRes) {
        if (lottieAnimationView != null) {
            lottieAnimationView.setAnimation(rawRes);
        }
    }

    private void initLottieView() {
        lottieAnimationView = new LottieAnimationViewDelegate(mContext) {
            @Override
            public void onTouchPointEvent(float x, float y) {
                LottieView.this.onTouchPointEvent(x, y);
            }
        };

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(lottieAnimationView, layoutParams);

        lottieAnimationView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                Log.d(TAG, "onCompositionLoaded: " + composition.getEndFrame());
            }
        });

        lottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate: " + animation.getDuration());
            }
        });

        lottieAnimationView.addAnimatorPauseListener(new Animator.AnimatorPauseListener() {
            @Override
            public void onAnimationPause(Animator animation) {
                Log.d(TAG, "onAnimationPause");
            }

            @Override
            public void onAnimationResume(Animator animation) {
                Log.d(TAG, "onAnimationResume");
            }
        });

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd");
                if (currentPlayFrame < frameConfigs.size()) {
                    playAnimationByConfig(frameConfigs.get(currentPlayFrame++));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "onAnimationCancel");

                if (currentPlayFrame < frameConfigs.size()) {
                    playAnimationByConfig(frameConfigs.get(currentPlayFrame++));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "onAnimationRepeat");
            }
        });
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (lottieAnimationView != null && scaleType != null) {
            lottieAnimationView.setScaleType(scaleType);
        }
    }

    public void playAnimation(boolean loop) {
        if (lottieAnimationView != null) {
            lottieAnimationView.setRepeatCount(loop ? ValueAnimator.INFINITE : 0);
            lottieAnimationView.playAnimation();
        }
    }

    public void playAnimationByConfig(final JsonParser.FrameConfig config) {
        if (lottieAnimationView != null && config != null) {

            lottieAnimationView.setRepeatCount(config.isLooper() ? ValueAnimator.INFINITE : 0);

            if (config.getDelayTime() > 0) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieAnimationView.setMinAndMaxFrame(config.getMinFrame(), config.getMaxFrame());
                        lottieAnimationView.playAnimation();

                        if (config.getPlayTime() != 0) {
                            cancelAnim(config.getPlayTime());
                        }
                    }
                }, config.getDelayTime());
            } else {
                lottieAnimationView.setMinAndMaxFrame(config.getMinFrame(), config.getMaxFrame());
                lottieAnimationView.playAnimation();
            }
        }
    }

    private void cancelAnim(int playTime) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.cancelAnimation();
            }
        }, playTime);
    }

    public void onTouchPointEvent(float x, float y) {
        if (mWebView != null) {
            String arg = x + "-" + y;
            callJsMethodByLoadUrl("javascript:onTouchPointEvent('" + arg + "')");
        }
    }

    @JavascriptInterface
    public void getInitAnimConfig(String str) {
        lottieAnimConfig = JsonParser.getLottieAnimConfig(str);
        if (lottieAnimConfig == null) {
            return;
        }

        configMap = lottieAnimConfig.getConfigMap();
        pathPropertyMap = lottieAnimConfig.getPathPropertyMap();
        frameConfigs = lottieAnimConfig.getFrameConfigs();

        if (pathPropertyMap != null) {
            setAnimTransForm(pathPropertyMap);
        }

        currentPlayFrame = 0;
        if (frameConfigs == null) {
            playAnimation(true);
        } else {
            playAnimationByConfig(frameConfigs.get(currentPlayFrame++));
        }
    }

    @JavascriptInterface
    public void getAnimConfig(String str, String clear) {
        lottieAnimConfig = JsonParser.getLottieAnimConfig(str);
        if (lottieAnimConfig == null) {
            return;
        }

        configMap = lottieAnimConfig.getConfigMap();
        pathPropertyMap = lottieAnimConfig.getPathPropertyMap();
        frameConfigs = lottieAnimConfig.getFrameConfigs();

        if (configMap != null) {
            setAnimBitmap(configMap, clear);
        }

        if (pathPropertyMap != null) {
            setAnimTransForm(pathPropertyMap);
        }

        currentPlayFrame = 0;

        if (frameConfigs == null) {
            playAnimation(true);
        } else {
            playAnimationByConfig(frameConfigs.get(currentPlayFrame++));
        }
    }

    @JavascriptInterface
    public void setText(String text, String newText) {
        if (lottieAnimationView != null && !TextUtils.isEmpty(text) && !TextUtils.isEmpty(newText)) {
            TextDelegate textDelegate = new TextDelegate(lottieAnimationView);
            lottieAnimationView.setTextDelegate(textDelegate);
            textDelegate.setText(text, newText);
        }
    }

    @JavascriptInterface
    public void onTouchEvent(String config, String arg) {
        Toast.makeText(getContext(), arg, Toast.LENGTH_SHORT).show();
    }

    private void setAnimBitmap(Map<String, String> configMap, String clear) {
        if (configMap == null) {
            return;
        }
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String res = entry.getValue();
            if ("clear".equals(clear)) {
                checkoutImg(configMap);
            }
            int resId = getRes(res);
            lottieAnimationView.updateBitmap(entry.getKey(), CommUtils.getBitmap(resId));
        }
    }

    private void setAnimTransForm(Map<String, KeyPathProperty> pathPropertyMap) {
        if (pathPropertyMap == null) {
            return;
        }
        for (Map.Entry<String, KeyPathProperty> entry : pathPropertyMap.entrySet()) {
            String keyPath = entry.getKey();
            KeyPathProperty keyPathProperty = entry.getValue();
            if ("anchor".equals(keyPathProperty.property)) {
                Log.d(TAG, keyPathProperty.toString());
                lottieAnimationView.addValueCallback(new KeyPath(keyPath), LottieProperty.TRANSFORM_ANCHOR_POINT, new LottieValueCallback<PointF>(new PointF(keyPathProperty.x, keyPathProperty.y)));
            }
        }
    }

    //临时方法
    private int getRes(String res) {
        int resId = 0;
        if ("bage".equals(res)) {
            resId = R.mipmap.bage;
        } else if ("beanbody".equals(res)) {
            resId = R.mipmap.beanbody;
        } else if ("rightarm2".equals(res)) {
            resId = R.mipmap.rightarm2;
        } else if ("rightarm".equals(res)) {
            resId = R.mipmap.rightarm;
        } else if ("cloth".equals(res)) {
            resId = R.mipmap.cloth;
        } else if ("leftarm2".equals(res)) {
            resId = R.mipmap.leftarm2;
        } else if ("leftarm".equals(res)) {
            resId = R.mipmap.leftarm;
        } else if ("hat2".equals(res)) {
            resId = R.mipmap.hat2;
        } else if ("hat".equals(res)) {
            resId = R.mipmap.hat;
        } else if ("pant".equals(res)) {
            resId = R.mipmap.pant;
        } else if ("pant2".equals(res)) {
            resId = R.mipmap.pant2;
        }
        return resId;
    }

    private void checkoutImg(Map<String, String> configMap) {
        if (lottieAnimationView == null) {
            return;
        }
        LottieComposition composition = lottieAnimationView.getComposition();
        if (composition == null) {
            return;
        }
        Map<String, LottieImageAsset> images = composition.getImages();
        for (Map.Entry<String, LottieImageAsset> entry : images.entrySet()) {
            String key = entry.getKey();
            if (!configMap.containsKey(key)) {
                LottieImageAsset lottieImageAsset = entry.getValue();
                lottieImageAsset.setBitmap(null);
            }
        }
    }

    //Native -> js
    @SuppressLint("ObsoleteSdkInt")
    public void callJsMethodByLoadUrl(final String url) {
        if (mWebView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 18) {
            mWebView.loadUrl(url);
        } else {
            mWebView.evaluateJavascript(url, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d(TAG, "url: " + url + " value: " + value);
                }
            });
        }
    }
}
