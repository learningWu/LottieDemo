package com.ws.lottie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.ws.lottie.lottie.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://airbnb.io/lottie/#/supported-features
//http://airbnb.io/lottie/#/android?id=dynamic-properties
public class MainActivity2 extends AppCompatActivity {

    private LottieAnimationView lottieView;
    private BitmapFactory.Options opts;
    private WebView contentWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        lottieView = findViewById(R.id.animation_view);
        findViewById(R.id.btn_hz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changCloths();
            }
        });

        findViewById(R.id.btn_hz2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changCloths2();
            }
        });


        contentWebView = new WebView(this);

        readLocalLottieJson();
        initLottieView();
        initWebView();

//        downloadLottieJson("http://192.168.1.6:3000/public/images/bean.json");
    }

    private void downloadLottieJson(String url) {
        HttpUtil.downloadFile(url);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.addJavascriptInterface(this, "ws");
        contentWebView.loadUrl("file:///android_asset/show.html");
        contentWebView.setWebChromeClient(new WebChromeClient());

//        lottieView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changCloths();
//            }
//        });
    }

    public void changCloths() {
        contentWebView.loadUrl("javascript:getAnimConfig()");
        contentWebView.loadUrl("javascript:setAnimTransForm()");
    }

    public void changCloths2() {
        contentWebView.loadUrl("javascript:getAnimConfig2()");
        contentWebView.loadUrl("javascript:setAnimTransForm2()");
    }

    @JavascriptInterface
    public void getAnimConfig(String str) {
        HashMap<String, String> configMap = parseImgJsonConfig(str);
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String res = entry.getValue();
            removeImg(configMap);
            int resId = getRes(res);
            try {
                lottieView.updateBitmap(entry.getKey(), BitmapFactory.decodeResource(MainActivity2.this.getResources(), resId, opts));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeImg(HashMap<String, String> configMap) {
        LottieComposition composition = lottieView.getComposition();
        Map<String, LottieImageAsset> images = composition.getImages();

        for (Map.Entry<String, LottieImageAsset> entry : images.entrySet()) {
            String key = entry.getKey();
            if (!configMap.containsKey(key)) {
                LottieImageAsset lottieImageAsset = entry.getValue();
                lottieImageAsset.setBitmap(null);
            }
        }
    }

    @JavascriptInterface
    public void setAnimTransForm(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray transformMap = jsonObject.getJSONArray("transformMap");
            for (int i = 0; i < transformMap.length(); i++) {
                JSONObject object = (JSONObject) transformMap.get(i);
                String keyPath = object.getString("keypath");
                String property = object.getString("property");
                int x = object.getInt("x");
                int y = object.getInt("y");
                lottieView.addValueCallback(new KeyPath(keyPath), LottieProperty.TRANSFORM_ANCHOR_POINT, new LottieValueCallback<PointF>(new PointF(x, y)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
        } else if ("hat3".equals(res)) {
            resId = R.mipmap.hat3;
        }
        return resId;
    }

    private HashMap<String, String> parseImgJsonConfig(String str) {

        HashMap<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray animMap = jsonObject.getJSONArray("animMap");
            for (int i = 0; i < animMap.length(); i++) {
                JSONObject object = (JSONObject) animMap.get(i);
                String id = object.getString("id");
                String res = object.getString("res");
                map.put(id, res);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void readLocalLottieJson() {
        File file = new File("/storage/emulated/0/bean.json");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int n = 0;
            StringBuilder sBuffer = new StringBuilder();
            while (n != -1)  //???n?????????-1,?????????????????????
            {
                n = fileInputStream.read();//???????????????????????????(8???????????????),???????????????????????????????????????????????????
                char by = (char) n; //????????????
                sBuffer.append(by);
            }
            String lottieJson = sBuffer.toString();
            lottieView.setAnimationFromJson(lottieJson, "beanLocal");
        } catch (FileNotFoundException e) {
            System.out.println("?????????????????????????????????????????????????????????");
        } catch (IOException e) {
            System.out.println("????????????????????????");
        }
    }

    private void initLottieView() {
        lottieView.setAnimation(R.raw.sign);

        // lottie ????????? url ????????????api
//        lottieView.setAnimationFromUrl("http://192.168.1.6:3000/public/images/bean.json", "bean");

        lottieView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // ???????????????????????? x, y
                    float x = event.getX();
                    float y = event.getY();
                    // ??????????????????
                    float dst[] = new float[2];
                    // ?????????ImageView???matrix
                    Matrix imageMatrix = lottieView.getImageMatrix();
                    // ?????????????????????
                    Matrix inverseMatrix = new Matrix();
                    // ???????????????????????????
                    imageMatrix.invert(inverseMatrix);
                    // ???????????????????????????????????? dst ??????
                    inverseMatrix.mapPoints(dst, new float[]{x, y});
                    float dstX = dst[0];
                    float dstY = dst[1];
                    if (contentWebView != null) {
                        String arg = x + "-" + y;
//                        Log.d("x-y", arg);
                        contentWebView.loadUrl("javascript:onTouchPointEvent('" + arg + "')");
                    }
                }
                return false;
            }
        });
        lottieView.setScale(0.4f);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;
        opts = new BitmapFactory.Options();
        opts.inScaled = true;
        opts.inDensity = densityDpi;

        // ?????????????????????????????????
        // 1.????????????????????????
//        lottieView.setImageAssetsFolder("images/");
        // 2.????????????????????????????????? asset(????????????) ??????????????????????????????
        lottieView.setImageAssetDelegate(new ImageAssetDelegate() {
            @Nullable
            @Override
            public Bitmap fetchBitmap(LottieImageAsset asset) {
//                System.out.println("ws asset id: " + asset.getId());
                String assetId = asset.getId();
                int res = 0;
                if ("image_0".equals(assetId)) {
                    res = R.mipmap.hat3;
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
                return BitmapFactory.decodeResource(MainActivity2.this.getResources(), res, opts);
            }
        });

        lottieView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                List<KeyPath> keyPaths = lottieView.resolveKeyPath(new KeyPath("**"));
                for (KeyPath keyPath : keyPaths) {
//                    Log.d("ws", keyPath.keysToString());
                }
            }
        });
    }
}
