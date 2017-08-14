package com.yonikal.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoni on 14/08/2017.
 */
public class AppStateListener implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = AppStateListener.class.getSimpleName();
    public static final long CHECK_DELAY = 500;

    public interface Listener {
        void onForeground();

        void onBackground();
    }

    private static AppStateListener mInstance;
    private boolean mIsForeground;
    private boolean mIsPaused;
    private Handler mHandler;
    private List<Listener> mListeners;
    private Runnable mCheck;
    private static Object LOCK = new Object();

    private AppStateListener() {
        mIsForeground = false;
        mIsPaused = true;
        mHandler = new Handler();
        mListeners = new ArrayList<>();
    }

    public static AppStateListener init(Application app) {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (mInstance == null) {
                    mInstance = new AppStateListener();
                    app.registerActivityLifecycleCallbacks(mInstance);
                }
            }
        }

        return mInstance;
    }

    public static AppStateListener getInstance() {
        return mInstance;
    }

    public boolean isForeground() {
        return mIsForeground;
    }

    public boolean isBackground() {
        return !mIsForeground;
    }

    public boolean isPaused() {
        return mIsPaused;
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        boolean wasBackground = !mIsForeground;
        mIsPaused = false;
        mIsForeground = true;

        if (mCheck != null) {
            mHandler.removeCallbacks(mCheck);
        }

        if (wasBackground) {
            for (Listener listener : mListeners) {
                try {
                    listener.onForeground();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mIsPaused = true;

        if (mCheck != null) {
            mHandler.removeCallbacks(mCheck);
        }

        mHandler.postDelayed(mCheck = new Runnable() {
            @Override
            public void run() {
                if (mIsForeground && mIsPaused) {
                    Log.i(TAG, "App is background");
                    mIsForeground = false;
                    for (Listener listener : mListeners) {
                        try {
                            listener.onBackground();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
