package com.example.android.blends.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.android.blends.R;

public class SplashActivity extends Activity {
    private Handler mHandler;
    private static final long TIME_OUT_SPLASH = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

}
