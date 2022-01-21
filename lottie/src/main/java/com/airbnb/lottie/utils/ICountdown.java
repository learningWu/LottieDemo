package com.airbnb.lottie.utils;

public interface ICountdown {

    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    void onFinish();

    void  onStart();
}
