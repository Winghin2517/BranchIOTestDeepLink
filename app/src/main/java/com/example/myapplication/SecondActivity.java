package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;

import io.branch.referral.Branch;

/**
 * Created by Simon on 12/17/2015.
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Branch.isAutoDeepLinkLaunch(this)) {
            try {

                Log.e("picurl",Branch.getInstance().getLatestReferringParams().getString("picurl"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("nondeeplink", "Launched by normal application flow");
        }

    }
}
