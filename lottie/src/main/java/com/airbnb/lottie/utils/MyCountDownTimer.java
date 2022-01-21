package com.airbnb.lottie.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;


public class MyCountDownTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    private ICountdown countdown;


    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
        countdown = null;
    }

    /**
     * Start the countdown.
     */
    public synchronized final void start(long millisInFuture, ICountdown countdown) {
        if (!mCancelled) {
            cancel();
        }

        this.countdown = countdown;

        if (countdown != null) {
            countdown.onTick(millisInFuture);
        }

        mCancelled = false;
        if (millisInFuture <= 0) {
            if (countdown != null) {
                countdown.onFinish();
            }
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + millisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (MyCountDownTimer.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    if (countdown != null) {
                        countdown.onFinish();
                    }
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    if (countdown != null) {
                        countdown.onTick(millisLeft);
                    }

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) {
                            delay = 0;
                        }
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) {
                            delay += mCountdownInterval;
                        }
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    public void destroy() {
        cancel();
        instance = null;
    }

    private static volatile MyCountDownTimer instance;

    public MyCountDownTimer(long countdownInterval) {
        mCountdownInterval = countdownInterval;
    }
}
