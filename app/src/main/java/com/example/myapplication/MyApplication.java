package com.example.myapplication;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by Simon on 12/16/2015.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Branch.getAutoInstance(this);
    }
}
