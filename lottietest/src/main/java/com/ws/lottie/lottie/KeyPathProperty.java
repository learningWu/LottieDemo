package com.ws.lottie.lottie;

public class KeyPathProperty {
    public String keyPath;
    public String property;
    public int x;
    public int y;

    public KeyPathProperty(String keyPath, String property, int x, int y) {
        this.keyPath = keyPath;
        this.property = property;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "KeyPathProperty{" +
                "keyPath='" + keyPath + '\'' +
                ", property='" + property + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
