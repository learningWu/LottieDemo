package com.ws.lottie.lottie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    static class FrameConfig {
        private int minFrame;
        private int maxFrame;
        private boolean looper;
        private int delayTime;
        private int playTime;

        public FrameConfig(int minFrame, int maxFrame, boolean looper, int delayTime, int playTime) {
            this.minFrame = minFrame;
            this.maxFrame = maxFrame;
            this.looper = looper;
            this.delayTime = delayTime;
            this.playTime = playTime;
        }

        public int getMinFrame() {
            return minFrame;
        }

        public void setMinFrame(int minFrame) {
            this.minFrame = minFrame;
        }

        public int getMaxFrame() {
            return maxFrame;
        }

        public void setMaxFrame(int maxFrame) {
            this.maxFrame = maxFrame;
        }

        public boolean isLooper() {
            return looper;
        }

        public void setLooper(boolean looper) {
            this.looper = looper;
        }

        public int getDelayTime() {
            return delayTime;
        }

        public void setDelayTime(int delayTime) {
            this.delayTime = delayTime;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        @Override
        public String toString() {
            return "FrameConfig{" +
                    "minFrame=" + minFrame +
                    ", maxFrame=" + maxFrame +
                    ", looper=" + looper +
                    ", delayTime=" + delayTime +
                    ", playTime=" + playTime +
                    '}';
        }
    }

    static class LottieAnimConfig {
        private Map<String, String> configMap;
        private Map<String, KeyPathProperty> pathPropertyMap;
        private List<FrameConfig> frameConfigs;

        public Map<String, String> getConfigMap() {
            return configMap == null || configMap.size() == 0 ? null : configMap;
        }

        public Map<String, KeyPathProperty> getPathPropertyMap() {
            return pathPropertyMap == null || pathPropertyMap.size() == 0 ? null : pathPropertyMap;
        }

        public List<FrameConfig> getFrameConfigs() {
            return frameConfigs == null || frameConfigs.size() == 0 ? null : frameConfigs;
        }
    }

    public static LottieAnimConfig getLottieAnimConfig(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        LottieAnimConfig lottieAnimConfig = new LottieAnimConfig();
        lottieAnimConfig.configMap = parseImgJsonConfig(jsonStr);
        lottieAnimConfig.pathPropertyMap = parseTransformJsonConfig(jsonStr);
        lottieAnimConfig.frameConfigs = parseFrameConfig(jsonStr);
        return lottieAnimConfig;
    }

    public static Map<String, String> parseImgJsonConfig(String str) {
        HashMap<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray animMap = jsonObject.optJSONArray("imgMap");
            if (animMap == null) {
                return null;
            }
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

    public static Map<String, KeyPathProperty> parseTransformJsonConfig(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray transformMap = jsonObject.optJSONArray("transformMap");
            if (transformMap == null) {
                return null;
            }
            HashMap<String, KeyPathProperty> propertyHashMap = new HashMap<>();
            for (int i = 0; i < transformMap.length(); i++) {
                JSONObject object = (JSONObject) transformMap.get(i);
                String keyPath = object.getString("keypath");
                String property = object.getString("property");
                int x = object.getInt("x");
                int y = object.getInt("y");
                propertyHashMap.put(keyPath, new KeyPathProperty(keyPath, property, x, y));
            }
            return propertyHashMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<FrameConfig> parseFrameConfig(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray frameMap = jsonObject.optJSONArray("frameMap");
            if (frameMap == null) {
                return null;
            }
            ArrayList<FrameConfig> frameConfigs = new ArrayList<>();
            for (int i = 0; i < frameMap.length(); i++) {
                JSONObject object = (JSONObject) frameMap.get(i);
                int min = object.optInt("min", 0);
                int max = object.optInt("max", 0);
                int delayTime = object.optInt("delayTime", 0);
                int playTime = object.optInt("playTime", 0);
                boolean looper = object.optBoolean("looper", false);
                FrameConfig frameConfig = new FrameConfig(min, max, looper, delayTime, playTime);
                frameConfigs.add(frameConfig);
            }
            return frameConfigs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
